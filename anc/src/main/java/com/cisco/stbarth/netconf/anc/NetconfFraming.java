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

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

class NetconfFraming {
    private static final ByteBuffer EOM = ByteBuffer.wrap("]]>]]>".getBytes());
    static {
        EOM.position(1);
    }

    static class DelimitedMessageUnframer extends InputStream {
        private ByteBuffer buffer = ByteBuffer.allocate(EOM.remaining());
        private boolean closed = false;
        private InputStream inputStream;

        DelimitedMessageUnframer(InputStream inputStream) {
            this.inputStream = inputStream;
            buffer.limit(0);
        }

        @Override
        public int read() throws IOException {
            int b = closed ? -1 : (buffer.hasRemaining() ? buffer.get() : inputStream.read());
            if (b == EOM.get(0)) {
                buffer.compact();
                while (buffer.hasRemaining()) {
                    int read = inputStream.read(buffer.array(), buffer.position(), buffer.remaining());
                    if (read < 0)
                        throw new IOException("Premature EOF");
                    else
                        buffer.position(buffer.position() + read);
                }

                buffer.rewind();
                if (buffer.equals(EOM)) {
                    closed = true;
                    b = -1;
                }
            }
            return b;
        }

        @Override
        public void close() throws IOException {
            while (read() >= 0);
        }
    }

    static class DelimitedMessageFramer extends FilterOutputStream {
        DelimitedMessageFramer(OutputStream outputStream) {
            super(outputStream);
        }

        @Override
        public void close() throws IOException {
            out.write(EOM.array());
            out.flush();
        }
    }

    static class ChunkedMessageUnframer extends InputStream {
        private InputStream inputStream;
        private byte[] dummy = new byte[1];
        private int remaining = 0;

        ChunkedMessageUnframer(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public int read() throws IOException {
            return read(dummy) < 0 ? -1 : dummy[0];
        }

        @Override
        public int read(byte[] b) throws IOException {
            return read(b, 0, b.length);
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            if (remaining < 0) {
                return -1;
            } else if (remaining == 0 && inputStream.read() == '\n' && inputStream.read() == '#') {
                int c;
                for (c = inputStream.read(); c >= '0' && c <= '9'; c = inputStream.read())
                    remaining = remaining * 10 + (c - '0');

                if (remaining == 0 && c == '#' && inputStream.read() == '\n') {
                    remaining = -1;
                    return -1;
                } else if (c != '\n') {
                    remaining = -1;
                }
            }

            if (remaining <= 0)
                throw new IOException("Invalid framing header");

            len = inputStream.read(b, off, Math.min(remaining, len));
            if (len < 0)
                throw new IOException("Premature EOF");

            remaining -= len;
            return len;
        }

        @Override
        public void close() throws IOException {
            while (read() >= 0);
        }
    }

    static class ChunkedMessageFramer extends FilterOutputStream {
        ChunkedMessageFramer(OutputStream outputStream) {
            super(outputStream);
        }

        @Override
        public void write(int b) throws IOException {
            out.write("\n#1\n".getBytes());
            out.write(b);
        }

        @Override
        public void write(byte[] b) throws IOException {
            write(b, 0, b.length);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            out.write(String.format("\n#%d\n", len).getBytes());
            out.write(b, off, len);
        }

        @Override
        public void close() throws IOException {
            out.write("\n##\n".getBytes());
            out.flush();
        }
    }
}
