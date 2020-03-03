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

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ChannelSubsystem;
import org.apache.sshd.client.future.AuthFuture;
import org.apache.sshd.client.future.ConnectFuture;
import org.apache.sshd.client.future.OpenFuture;
import org.apache.sshd.client.keyverifier.AcceptAllServerKeyVerifier;
import org.apache.sshd.client.keyverifier.RejectAllServerKeyVerifier;
import org.apache.sshd.client.keyverifier.RequiredServerKeyVerifier;
import org.apache.sshd.client.keyverifier.ServerKeyVerifier;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.session.SessionHeartbeatController.HeartbeatType;

import java.io.IOException;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.concurrent.TimeUnit;

public class NetconfSSHClient extends NetconfClient {
    private SshClient client;
    private String hostname;
    private String username;
    private String password;
    private KeyPair keypair;
    private ServerKeyVerifier verifier = RejectAllServerKeyVerifier.INSTANCE;
    private int port;
    private int timeout = 5000;

    /**
     * Create a new NETCONF connection to the given server
     *
     * The connection will not be established immediately, but instead upon creating the first session.
     *
     * @param hostname
     * @param port
     * @param username
     */
    public NetconfSSHClient(String hostname, int port, String username) {
        this.hostname = hostname;
        this.port = port;
        this.username = username;

        client = SshClient.setUpDefaultClient();
        client.setSessionHeartbeat(HeartbeatType.IGNORE, TimeUnit.MILLISECONDS, 5000);
        client.start();
    }

    /**
     * Use password authentication for the SSH session
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Set the SSH connection timeout in ms
     * @param timeout timeout in milliseconds
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * Set the SSH keepalive interval in ms
     * @param keepalive interval in milliseconds
     */
    public void setKeepalive(int keepalive) {
        if (keepalive <= 0)
            client.setSessionHeartbeat(HeartbeatType.NONE, TimeUnit.MILLISECONDS, 0);
        else
            client.setSessionHeartbeat(HeartbeatType.IGNORE, TimeUnit.MILLISECONDS, keepalive);
    }

    /**
     * Use a private key to authenticate.
     */
    public void setKeyPair(KeyPair keypair) {
        this.keypair = keypair;
    }

    /**
     * Set SSH host key to verify
     * 
     * @param key
     */
    public void setServerKey(PublicKey key) {
        verifier = new RequiredServerKeyVerifier(key);
        setStrictHostKeyChecking(true);
    }

    /**
     * Enable or disable strict hostkey checking
     *
     * This must be done before createSession is called
     *
     * @param strictHostKeyChecking
     */
    public void setStrictHostKeyChecking(boolean strictHostKeyChecking) {
        if (strictHostKeyChecking) {
            client.setServerKeyVerifier(verifier);
        } else {
            client.setServerKeyVerifier(AcceptAllServerKeyVerifier.INSTANCE);
        }
    }

    /**
     * Create a new NETCONF session (and create the underlying SSH connection if it didn't already exist).
     * @return
     * @throws NetconfException.ProtocolException
     */
    public synchronized NetconfSession createSession() throws NetconfException.ProtocolException {
        ClientSession session;
        try {
            ConnectFuture connect = client.connect(this.username, this.hostname, this.port);
            connect.verify(timeout);
            session = connect.getSession();
        } catch (IOException e) {
            throw new NetconfException.ProtocolException(e);
        }

        if (keypair != null)
            session.addPublicKeyIdentity(keypair);

        if (password != null)
            session.addPasswordIdentity(password);

        try {
            AuthFuture auth = session.auth().verify(timeout);
            if (!auth.isSuccess())
                throw auth.getException();

            ChannelSubsystem channel = session.createSubsystemChannel("netconf");
            OpenFuture open = channel.open().verify(timeout);
            if (!open.isOpened())
                throw open.getException();

            NetconfSession netconfSession = new NetconfSession(
                    this, channel.getInvertedOut(), channel.getInvertedIn(), session::close);
            netconfSession.hello();
            return netconfSession;
        } catch (Throwable e) {
            try {
                session.close();
            } catch (IOException f) {}

            throw (e instanceof NetconfException.ProtocolException) ?
                (NetconfException.ProtocolException)e : new NetconfException.ProtocolException(e);
        }
    }

    /**
     * Close the NETCONF connection to the server including any remaining sessions.
     */
    public void close() {
        client.stop();
    }
}
