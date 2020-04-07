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

public class NetconfException extends Exception {
    public NetconfException(Throwable e) {
        super(e);
    }

    public NetconfException(String msg) {
        super(msg);
    }

    /**
     * A low-level protocol or I/O exception.
     *
     * Upon catching such an exception, the behavior of the session becomes undefined.
     */
    public static class ProtocolException extends NetconfException {
        public ProtocolException(Throwable e) {
            super(e);
        }

        public ProtocolException(String msg) {
            super(msg);
        }
    }

    /**
     * A high-level RPC exception.
     */
    public static class RPCException extends NetconfException {
        private XMLElement rpcReply;

        public RPCException(XMLElement rpcReply) {
            super(rpcReply.getFirst("rpc-error").map(x -> x.getText("error-tag")).orElse("") + ": " +
                    rpcReply.getFirst("rpc-error").map(x -> x.getText("error-message")).orElse("") + " (" +
                    rpcReply.getFirst("rpc-error").map(x -> x.getText("error-info")).orElse("") + ")");
            this.rpcReply = rpcReply;
        }

        public XMLElement getRPCReply() {
            return rpcReply;
        }
    }
}
