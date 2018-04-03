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

import com.jcraft.jsch.ChannelSubsystem;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;

public class NetconfSSHClient extends NetconfClient {
    private Session session;
    private String hostname;
    private String username;
    private String password;
    private String keyfile;
    private String keyfilePassphrase;
    private boolean strictHostKeyChecking = true;
    private int port;
    private int timeout;
    private int keepalive;

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
        this.keepalive = keepalive;
    }

    /**
     * Use a private key to authenticate and use the password (if given) to decrypt it.
     * @param keyFile
     * @param keyFilePassphrase
     */
    public void setKeyFile(String keyFile, String keyFilePassphrase) {
        this.keyfile = keyFile;
        this.keyfilePassphrase = keyFilePassphrase;
    }

    /**
     * Enable or disable strict hostkey checking
     *
     * This must be done before createSession is called
     *
     * @param strictHostKeyChecking
     */
    public void setStrictHostKeyChecking(boolean strictHostKeyChecking) {
        this.strictHostKeyChecking = strictHostKeyChecking;
    }

    /**
     * Create a new NETCONF session (and create the underlying SSH connection if it didn't already exist).
     * @return
     * @throws NetconfException.ProtocolException
     */
    public NetconfSession createSession() throws NetconfException.ProtocolException {
        try {
            if (session == null || !session.isConnected()) {
                JSch jSch = new JSch();

                if (keyfile != null && keyfilePassphrase != null)
                    jSch.addIdentity(keyfile, keyfilePassphrase);
                else if (keyfile != null)
                    jSch.addIdentity(keyfile);

                session = jSch.getSession(username, hostname, port);
                session.setDaemonThread(true);
                session.setTimeout(timeout);
                session.setServerAliveInterval(keepalive);
                session.setConfig("StrictHostKeyChecking", strictHostKeyChecking ? "yes" : "no");

                if (password != null)
                    session.setPassword(password);

                session.connect();
            }

            ChannelSubsystem channel = (ChannelSubsystem)session.openChannel("subsystem");
            channel.setSubsystem("netconf");
            NetconfSession netconfSession = new NetconfSession(
                    this, channel.getInputStream(), channel.getOutputStream(), channel::disconnect);
            channel.connect();
            netconfSession.hello();
            return netconfSession;
        } catch (JSchException | IOException e) {
            throw new NetconfException.ProtocolException(e);
        }
    }

    /**
     * Close the NETCONF connection to the server including any remaining sessions.
     */
    public void close() {
        session.disconnect();
    }
}
