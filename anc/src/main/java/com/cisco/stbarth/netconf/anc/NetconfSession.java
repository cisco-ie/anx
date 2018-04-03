/**
 * Copyright (c) 2018 Cisco Systems
 * 
 * Author: Steven Barth <stbarth@cisco.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cisco.stbarth.netconf.anc;

import java.io.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.cisco.stbarth.netconf.anc.Netconf.*;

public class NetconfSession implements AutoCloseable {
    private NetconfClient client;
    private InputStream inputStream;
    private OutputStream outputStream;
    private AutoCloseable closeableTransport;
    private Map<String,String> capabilities;
    private long sessionID;
    private int messageID;
    private Netconf.DefaultsMode defaultsMode;
    private Netconf.NCSCommitParameter ncsCommitParameter;
    private boolean closed;
    private Consumer<XMLElement> notificationConsumer;
    private BiConsumer<XMLElement,XMLElement> callTraceConsumer;

    private Function<InputStream,InputStream> unframingFactory = NetconfFraming.DelimitedMessageUnframer::new;
    private Function<OutputStream,OutputStream> framingFactory = NetconfFraming.DelimitedMessageFramer::new;

    NetconfSession(NetconfClient client, InputStream input, OutputStream output, AutoCloseable closeableTransport) {
        this.client = client;
        this.inputStream = input;
        this.outputStream = output;
        this.closeableTransport = closeableTransport;
    }

    void hello() throws NetconfException.ProtocolException {
        XMLElement peerHello = request(new XMLElement(NS_NETCONF, "hello")
                .createChild("capabilities")
                    .withTextChild("capability", CAP_NETCONF_10)
                    .withTextChild("capability", CAP_NETCONF_11));

        sessionID = Long.parseLong(peerHello.getText("session-id"));
        capabilities = new HashMap<>();
        peerHello.getFirst("capabilities").map(x -> x.stream("capability")).orElse(Stream.empty())
            .map(XMLElement::getText).forEach(x -> capabilities.put(
                x.indexOf('?') < 0 ? x : x.substring(0, x.indexOf('?')),
                x.indexOf('?') < 0 ? "" : x.substring(x.indexOf('?'))));

        if (capabilities.containsKey(CAP_NETCONF_11)) {
            framingFactory = NetconfFraming.ChunkedMessageFramer::new;
            unframingFactory = NetconfFraming.ChunkedMessageUnframer::new;
        } else if (!capabilities.containsKey(CAP_NETCONF_10)) {
            throw new NetconfException.ProtocolException("No supported protocol versions in common!");
        }
    }

    /**
     * Set with-defaults parameter for handling of default values for data retrieval operations.
     * @param defaultsMode
     */
    public void setDefaultsMode(DefaultsMode defaultsMode) {
        this.defaultsMode = defaultsMode;
    }

    /**
     * Add NCS-specific commit parameter
     * @param ncsCommitParameter
     */
    public void setNCSCommitParameter(NCSCommitParameter ncsCommitParameter) {
        this.ncsCommitParameter = ncsCommitParameter;
    }

    private void send(XMLElement element) throws NetconfException.ProtocolException {
        try (OutputStream messageOutputStream = framingFactory.apply(outputStream)) {
            element.writeTo(messageOutputStream, true);
        } catch (IOException | XMLElement.XMLException e) {
            throw new NetconfException.ProtocolException(e);
        }
    }

    private synchronized XMLElement request(XMLElement element) throws NetconfException.ProtocolException {
        send(element);
        return receive();
    }

    /**
     * Receive one message from the server.
     *
     * This can be used for receiving notifications, once a subscription has been created.
     *
     * @return XML element
     * @throws NetconfException.ProtocolException
     */
    public synchronized XMLElement receive() throws NetconfException.ProtocolException {
        try {
            return new XMLElement(unframingFactory.apply(inputStream));
        } catch (IOException | XMLElement.XMLException e) {
            throw new NetconfException.ProtocolException(e);
        }
    }

    /**
     * Set the consumer for notifications
     *
     * This is used as callback whenever notifications are received, e.g. when reusing a session for both notifications
     * and requests or to avoid a race condition when setting up multiple subscriptions. This callback is also used
     * for receiveNotifications().
     *
     * @param notificationConsumer
     */
    public void setNotificationConsumer(Consumer<XMLElement> notificationConsumer) {
        this.notificationConsumer = notificationConsumer;
    }

    /**
     * Set the consumer for call-traces
     *
     * This is a debug mechanism which can trace all request / response messages done by this session.
     * @param callTraceConsumer
     */
    public void setCallTraceConsumer(BiConsumer<XMLElement, XMLElement> callTraceConsumer) {
        this.callTraceConsumer = callTraceConsumer;
    }

    /**
     * Receive all notifications from the server, until a non-notification is received which is returned.
     *
     * setNotificationConsumer is used to setup the callback.
     *
     * @return the first non-notification reply
     * @throws NetconfException.ProtocolException
     */
    public XMLElement receiveNotifications() throws NetconfException.ProtocolException {
        XMLElement message = receive();
        while (message.getNamespace().equals(NS_NETCONF_NOTIFICATION) && message.getName().equals("notification")) {
            notificationConsumer.accept(message);
            message = receive();
        }
        return message;
    }

    /**
     * Send a RPC request to the NETCONF server and receive the reply.
     *
     * @param request Request element
     * @return The RPC reply in case of success
     * @throws NetconfException RPCException with data supplied from the server or a ProtocolException indicating lower-level errors
     */
    public synchronized XMLElement call(XMLElement request) throws NetconfException {
        String rpcID = String.valueOf(++messageID);
        XMLElement call = new XMLElement(NS_NETCONF, "rpc").withAttribute("message-id", rpcID).withChild(request);
        XMLElement reply = request(call);

        while (reply.getNamespace().equals(NS_NETCONF_NOTIFICATION) && reply.getName().equals("notification")) {
            if (notificationConsumer != null)
                notificationConsumer.accept(reply);
            reply = receive();
        }

        if (callTraceConsumer != null)
            callTraceConsumer.accept(call, reply);

        if (!reply.getName().equals("rpc-reply"))
            throw new NetconfException.ProtocolException("Invalid RPC-reply received");

        if (reply.getFirst("rpc-error").isPresent())
            throw new NetconfException.RPCException(reply);

        return reply;
    }

    private XMLElement get(Datastore datastore, Consumer<XMLElement> filter)
            throws NetconfException {
        return call(new XMLElement(NS_NETCONF, datastore != null ? "get-config" : "get", gc -> {
                if (datastore != null)
                    gc.createChild("source").createChild(datastore.name().toLowerCase());

                if (filter != null)
                    gc.withChild("filter", filter);

                if (defaultsMode != null)
                    gc.withTextChild(NS_NETCONF_WITH_DEFAULTS, "with-defaults",
                            defaultsMode.name().toLowerCase().replace('_', '-'));
        })).getFirst("data").orElseThrow(() -> new NetconfException("Missing data element in reply to <get>"));
    }

    /**
     * Perform a get-config operation using an XPath filter
     * @param xpathFilter   XPath-filter to apply remotely
     * @param datastore     If non-null, perform a get-config operation on given datastore, otherwise perform a get
     * @return Data element of the rpc-reply
     * @throws NetconfException RPCException or Protocol
     */
    public XMLElement getConfig(Datastore datastore, String xpathFilter) throws NetconfException {
        return get(datastore, f -> f
                .withAttribute("type", "xpath")
                .withAttribute("select", xpathFilter));
    }

    /**
     * Perform a get-config operation using an XML subtree filter
     * @param subtreeFilter   XPath-filter to apply remotely
     * @param datastore     If non-null, perform a get-config operation on given datastore, otherwise perform a get
     * @return Data element of the rpc-reply
     * @throws NetconfException RPCException or Protocol
     */
    public XMLElement getConfig(Datastore datastore, Iterable<XMLElement> subtreeFilter) throws NetconfException {
        return get(datastore, f -> f
                .withAttribute("type", "subtree")
                .withChildren(subtreeFilter));
    }

    /**
     * Perform a get-config operation without any filter
     * @param datastore     If non-null, perform a get-config operation on given datastore, otherwise perform a get
     * @return Data element of the rpc-reply
     * @throws NetconfException RPCException or Protocol
     */
    public XMLElement getConfig(Datastore datastore) throws NetconfException {
        return get(datastore, null);
    }

    /**
     * Perform a get operation using an XPath filter
     * @param xpathFilter   XPath-filter to apply remotely
     * @return Data element of the rpc-reply
     * @throws NetconfException RPCException or Protocol
     */
    public XMLElement get(String xpathFilter) throws NetconfException {
        return get(null, f -> f
                .withAttribute("type", "xpath")
                .withAttribute("select", xpathFilter));
    }

    /**
     * Perform a get operation using an XML subtree filter
     * @param subtreeFilter   XPath-filter to apply remotely
     * @return Data element of the rpc-reply
     * @throws NetconfException RPCException or Protocol
     */
    public XMLElement get(Iterable<XMLElement> subtreeFilter) throws NetconfException {
        return get(null, f -> f
                .withAttribute("type", "subtree")
                .withChildren(subtreeFilter));
    }

    /**
     * Perform a get operation without any filter
     * @return Data element of the rpc-reply
     * @throws NetconfException RPCException or Protocol
     */
    public XMLElement get() throws NetconfException {
        return get(null, null);
    }

    /**
     * Perform an edit-config opetation on the given datastore.
     * @param datastore         Target datastore
     * @param config            Config elements to manipulate
     * @param defaultOperation  If non-null, the default operation to perform
     * @param onError           If non-null, the behavior for error cases
     * @param testOption        If non-null, the behavior for testing
     * @throws NetconfException
     */
    public void editConfig(Datastore datastore, XMLElement config, EditConfigDefaultOperation defaultOperation,
                           EditConfigOnErrorOption onError, EditConfigTestOption testOption) throws NetconfException {
        call(new XMLElement(NS_NETCONF, "edit-config", ec -> {
            ec.withChild("target", t -> t
                    .withChild(datastore.name().toLowerCase()))
                    .withChild("config", c -> c.withChild(config));

            if (defaultOperation != null)
                ec.withTextChild("default-operation", defaultOperation.name().toLowerCase());

            if (onError != null)
                ec.withTextChild("error-option", onError.name().toLowerCase().concat("-on-error"));

            if (testOption != null)
                ec.withTextChild("test-option", testOption.name().toLowerCase().replace('_', '-'));

            if (ncsCommitParameter != null)
                ec.withChild(NS_NCS, ncsCommitParameter.name().toLowerCase().replace('_', '-'));
        }));
    }

    /**
     * Perform an edit-config opetation on the given datastore.
     * @param datastore         Target datastore
     * @param config            Config elements to manipulate
     * @param defaultOperation  If non-null, the default operation to perform
     * @throws NetconfException
     */
    public void editConfig(Datastore datastore, XMLElement config, EditConfigDefaultOperation defaultOperation)
            throws NetconfException {
        editConfig(datastore, config, defaultOperation, null, null);
    }

    /**
     * Perform an edit-config opetation on the given datastore.
     * @param datastore         Target datastore
     * @param config            Config elements to manipulate
     * @throws NetconfException
     */
    public void editConfig(Datastore datastore, XMLElement config) throws NetconfException {
        editConfig(datastore, config, null, null, null);
    }

    /**
     * Copy configuration from one datastore to another
     * @param source
     * @param target
     * @throws NetconfException
     */
    public void copyConfig(Datastore source, Datastore target) throws NetconfException {
        call(new XMLElement(NS_NETCONF, "copy-config", cc -> cc
                .withChild("source", s -> s.withChild(source.name().toLowerCase()))
                .withChild("target", t -> t.withChild(target.name().toLowerCase()))));
    }

    /**
     * Copy configuration from a datastore to a URL
     * @param source
     * @param targetURL
     * @throws NetconfException
     */
    public void copyConfig(Datastore source, String targetURL) throws NetconfException {
        call(new XMLElement(NS_NETCONF, "copy-config", cc -> {
                cc.withChild("source", s -> s.withChild(source.name().toLowerCase()))
                        .withChild("target", t -> t.withTextChild("url", targetURL));

                if (defaultsMode != null)
                    cc.withTextChild(NS_NETCONF_WITH_DEFAULTS, "with-defaults",
                            defaultsMode.name().toLowerCase().replace('_', '-'));
        }));
    }

    /**
     * Copy configuration from a URL to a datastore
     * @param sourceURL
     * @param target
     * @throws NetconfException
     */
    public void copyConfig(String sourceURL, Datastore target) throws NetconfException {
        call(new XMLElement(NS_NETCONF, "copy-config", cc -> cc
                .withChild("source", s -> s.withTextChild("url", sourceURL))
                .withChild("target", t -> t.withChild(target.name().toLowerCase()))));
    }

    /**
     * Copy configuration remote to remote
     * @param sourceURL
     * @param targetURL
     * @throws NetconfException
     */
    public void copyConfig(String sourceURL, String targetURL) throws NetconfException {
        call(new XMLElement(NS_NETCONF, "copy-config", cc -> cc
                .withChild("source", s -> s.withTextChild("url", sourceURL))
                .withChild("target", t -> t.withTextChild("url", targetURL))));
    }

    /**
     * Delete configuration from a datastore
     * @param target
     * @throws NetconfException
     */
    public void deleteConfig(Datastore target) throws NetconfException {
        call(new XMLElement(NS_NETCONF, "delete-config").withChild("target",
                x -> x.createChild(target.name().toLowerCase())));
    }

    /**
     * Delete configuration from a URL
     * @param targetURL
     * @throws NetconfException
     */
    public void deleteConfig(String targetURL) throws NetconfException {
        call(new XMLElement(NS_NETCONF, "unlock").withChild("target",
                x -> x.withTextChild("url", targetURL)));
    }

    /**
     * Lock given datastore
     * @param target
     * @throws NetconfException
     */
    public void lock(Datastore target) throws NetconfException {
        call(new XMLElement(NS_NETCONF, "lock").withChild("target",
                x -> x.createChild(target.name().toLowerCase())));
    }

    /**
     * Unlocak given datastore
     * @param target
     * @throws NetconfException
     */
    public void unlock(Datastore target) throws NetconfException {
        call(new XMLElement(NS_NETCONF, "unlock").withChild("target",
                x -> x.createChild(target.name().toLowerCase())));
    }

    /**
     * Kill given NETCONF session
     * @param sessionID
     * @throws NetconfException
     */
    public void killSession(long sessionID) throws NetconfException {
        call(new XMLElement(NS_NETCONF, "kill-session").withTextChild("session-id", String.valueOf(sessionID)));
    }

    /**
     * Commit the candidate configuration as the device's new current configuration.
     * @throws NetconfException
     */
    public void commit() throws NetconfException {
        call(new XMLElement(NS_NETCONF, "commit"));
    }

    /**
     * Create a confirmed commit
     * @param confirmTimeout    If greater than 0, also set the confirmation timeout to the given value in seconds
     * @param persist           If non-null, make the commit persistent with the given ID
     * @throws NetconfException
     */
    public void commit(long confirmTimeout, String persist) throws NetconfException {
        call(new XMLElement(NS_NETCONF, "commit", c -> {
            c.withChild("confirmed");

            if (confirmTimeout > 0)
                c.withTextChild("confirm-timeout", String.valueOf(confirmTimeout > 0 ? confirmTimeout : 600));

            if (persist != null)
                c.withTextChild("persist", persist);
        }));
    }

    /**
     * Confirm a persistent commit with given ID (likely from another Session)
     * @param persistID
     * @throws NetconfException
     */
    public void commit(String persistID) throws NetconfException {
        call(new XMLElement(NS_NETCONF, "commit").withTextChild("persist-id", persistID));
    }

    /**
     * Cancel a given commit
     * @param persistID If non-null, the persistent commit identifier (likely from another Session)
     * @throws NetconfException
     */
    public void cancelCommit(String persistID) throws NetconfException {
        call(new XMLElement(NS_NETCONF, "cancel-commit").withTextChild("persist-id", persistID));
    }

    /**
     * Revert the candidate configuration to the current running configuration.
     * @throws NetconfException
     */
    public void discardChanges() throws NetconfException {
        call(new XMLElement(NS_NETCONF, "discard-changes"));
    }

    /**
     * Validate given datastore
     * @param source
     * @throws NetconfException
     */
    public void validate(Datastore source) throws NetconfException {
        call(new XMLElement(NS_NETCONF, "validate").withChild("source",
                x -> x.createChild(source.name().toLowerCase())));
    }

    /**
     * Validate given remote configuration.
     * @param sourceURL
     * @throws NetconfException
     */
    public void validate(String sourceURL) throws NetconfException {
        call(new XMLElement(NS_NETCONF, "validate").withChild("source",
                x -> x.withTextChild("url", sourceURL)));
    }

    /**
     * Validate given configuration passed by value
     * @param config
     * @throws NetconfException
     */
    public void validate(XMLElement config) throws NetconfException {
        call(new XMLElement(NS_NETCONF, "validate").withChild("source",
                x -> x.createChild("config").withChild(config)));
    }

    private void createSubscription(String stream, ZonedDateTime startTime, ZonedDateTime stopTime,
                                    Consumer<XMLElement> consumer) throws NetconfException {
        call(new XMLElement(NS_NETCONF_NOTIFICATION, "create-subscription", cs -> {
            if (stream != null)
                cs.withTextChild("stream", stream);

            if (consumer != null)
                cs.withChild(NS_NETCONF, "filter", consumer);

            if (startTime != null)
                cs.withTextChild("startTime", startTime.format(DateTimeFormatter.ISO_INSTANT));

            if (stopTime != null)
                cs.withTextChild("stopTime", stopTime.format(DateTimeFormatter.ISO_INSTANT));
        }));
    }

    /**
     * Create a subscription for receiving notifications.
     * @param stream    if not-null, the stream to subscribe to, otherwise "NETCONF" is used
     * @param startTime if not-null, the time to start replaying from
     * @param stopTime  if not-null, the time until the subscription ends
     * @throws NetconfException
     */
    public void createSubscription(String stream, ZonedDateTime startTime, ZonedDateTime stopTime)
            throws NetconfException {
        createSubscription(stream, startTime, stopTime, null);
    }

    /**
     * Create a subscription for receiving notifications and filter using XPath.
     * @param stream    if not-null, the stream to subscribe to, otherwise "NETCONF" is used
     * @param xpathFilter XPath filter to apply
     * @param startTime if not-null, the time to start replaying from
     * @param stopTime  if not-null, the time until the subscription ends
     * @throws NetconfException
     */
    public void createSubscription(String stream, String xpathFilter, ZonedDateTime startTime, ZonedDateTime stopTime)
            throws NetconfException {
        createSubscription(stream, startTime, stopTime, f -> f.withAttribute("type", "xpath")
                .withAttribute("select", xpathFilter));
    }

    /**
     * Create a subscription for receiving notifications and filter using subtree.
     * @param stream    if not-null, the stream to subscribe to, otherwise "NETCONF" is used
     * @param subtreeFilter Subtree filter to apply
     * @param startTime if not-null, the time to start replaying from
     * @param stopTime  if not-null, the time until the subscription ends
     * @throws NetconfException
     */
    public void createSubscription(String stream, XMLElement subtreeFilter,
                                   ZonedDateTime startTime, ZonedDateTime stopTime) throws NetconfException {
        createSubscription(stream, startTime, stopTime,
                f -> f.withAttribute("type", "subtree").withChild(subtreeFilter));
    }

    /**
     * Retrieve a schema from a NETCONF server.
     * @param identifier    Schema to retrieve
     * @param version       if not-null, the version to retrieve
     * @param format        if not-null, the format to receive, otherwise "yang" is used
     * @throws NetconfException
     * @return Yang schema
     */
    public XMLElement getSchema(String identifier, String version, String format) throws NetconfException {
        return call(new XMLElement(NS_NETCONF_MONITORING, "get-schema", gs -> {
            gs.withTextChild("identifier", identifier);

            if (version != null)
                gs.withTextChild("version", version);

            if (format != null)
                gs.withTextChild("format", format);
        })).getFirst(NS_NETCONF_MONITORING, "data")
                .orElseThrow(() -> new NetconfException("No data-element in reply to get-schema"));
    }

    /**
     * Call a tailf:action.
     * @param data      Data
     * @throws NetconfException
     * @return Action Reply
     */
    public XMLElement tailfAction(XMLElement data) throws NetconfException {
        return call(new XMLElement(NS_TAILF_ACTIONS, "action").withChild("data", x -> x.withChild(data)))
                .getFirst("data").orElseThrow(() -> new NetconfException("Missing data element in reply to <get>"));
    }

    /**
     * Get the client object associated with this session.
     * @return
     */
    public NetconfClient getClient() {
        return client;
    }

    /**
     * Get the capabilities as indicated by the server
     * @return Mapping of capability (without arguments) to capability arguments (URI query string)
     */
    public Map<String,String> getCapabilities() {
        return capabilities;
    }

    /**
     * Get the ID of the current session
     * @return
     */
    public long getSessionID() {
        return sessionID;
    }

    /**
     * Check if the session was closed
     * @return
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * Gracefully shutdown the current session
     * @throws NetconfException.ProtocolException
     */
    public void close() throws NetconfException.ProtocolException {
        try {
            closed = true;
            send(new XMLElement(NS_NETCONF, "close-session"));
        } finally {
            try {
                closeableTransport.close();
            } catch (Exception e) {
                throw new NetconfException.ProtocolException(e);
            }
        }
    }
}