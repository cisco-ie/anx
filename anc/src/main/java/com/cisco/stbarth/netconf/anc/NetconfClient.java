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

import java.time.ZonedDateTime;
import java.util.function.Consumer;

public abstract class NetconfClient implements AutoCloseable {
    /**
     * Create a new session
     * @return
     * @throws NetconfException
     */
    public abstract NetconfSession createSession() throws NetconfException;

    /**
     * Close the current client
     * @throws NetconfException
     */
    public abstract void close() throws NetconfException;

    private AutoCloseable createSubscriptionThread(NetconfSession session, Consumer<XMLElement> notificationConsumer,
                                                   Consumer<NetconfException.ProtocolException> terminationConsumer) {
        session.setNotificationConsumer(notificationConsumer);
        Thread thread = new Thread(() -> {
            NetconfException.ProtocolException error = null;
            try {
                session.receiveNotifications();
            } catch (NetconfException.ProtocolException e) {
                if (!session.isClosed())
                    error = e;
            } finally {
                try {
                    synchronized (session) {
                        if (!session.isClosed())
                            session.close();
                    }
                } catch (NetconfException.ProtocolException e) {
                    // pass
                }
            }
            if (terminationConsumer != null)
                terminationConsumer.accept(error);
        });
        thread.setDaemon(true);
        thread.start();
        return session;
    }

    /**
     * Create a new subscription session, subscribe to the given notifications and spawn a new thread for consumption.
     *
     * @param stream    if not-null, the stream to subscribe to, otherwise "NETCONF" is used
     * @param startTime the time to start replaying from
     * @param stopTime  the time until the subscription ends
     * @param notificationConsumer Callback which will be invoked for each received notification
     * @param terminationConsumer Callback which will be invoked after the session has ended with the termination cause
     * @throws NetconfException
     * @return AutoCloseable object to close the subscription and end the underlying session
     */
    public AutoCloseable createSubscriptionSession(String stream, ZonedDateTime startTime, ZonedDateTime stopTime,
                                   Consumer<XMLElement> notificationConsumer,
                                   Consumer<NetconfException.ProtocolException> terminationConsumer)
            throws NetconfException {
        NetconfSession session = createSession();
        session.createSubscription(stream, startTime, stopTime);
        return createSubscriptionThread(session, notificationConsumer, terminationConsumer);
    }

    /**
     * Create a new subscription session, subscribe to the given notifications and spawn a new thread for consumption.
     *
     * Use XPath to filter notifications.
     *
     * @param stream
     * @param xpathFilter
     * @param startTime
     * @param stopTime
     * @param notificationConsumer Callback which will be invoked for each received notification
     * @param terminationConsumer Callback which will be invoked after the session has ended with the termination cause
     * @throws NetconfException
     * @return AutoCloseable object to close the subscription and end the underlying session
     */
    public AutoCloseable createSubscriptionSession(String stream, String xpathFilter, ZonedDateTime startTime,
                                            ZonedDateTime stopTime, Consumer<XMLElement> notificationConsumer,
                                            Consumer<NetconfException.ProtocolException> terminationConsumer)
            throws NetconfException {
        NetconfSession session = createSession();
        session.createSubscription(stream, xpathFilter, startTime, stopTime);
        return createSubscriptionThread(session, notificationConsumer, terminationConsumer);
    }


    /**
     * Create a new subscription session, subscribe to the given notifications and spawn a new thread for consumption.
     *
     * Use a subtree filter for filtering notifications.
     *
     * @param stream    if not-null, the stream to subscribe to, otherwise "NETCONF" is used
     * @param subtreeFilter Subtree filter to apply
     * @param startTime if not-null, the time to start replaying from
     * @param stopTime  if not-null, the time until the subscription ends
     * @param notificationConsumer Callback which will be invoked for each received notification
     * @param terminationConsumer Callback which will be invoked after the session has ended with the termination cause
     * @throws NetconfException
     * @return AutoCloseable object to close the subscription and end the underlying session
     */
    public AutoCloseable createSubscriptionSession(String stream, XMLElement subtreeFilter,
                                                   ZonedDateTime startTime, ZonedDateTime stopTime,
                                                   Consumer<XMLElement> notificationConsumer,
                                                   Consumer<NetconfException.ProtocolException> terminationConsumer)
            throws NetconfException {
        NetconfSession session = createSession();
        session.createSubscription(stream, subtreeFilter, startTime, stopTime);
        return createSubscriptionThread(session, notificationConsumer, terminationConsumer);
    }
}

