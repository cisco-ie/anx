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
package com.cisco.stbarth.netconf.xrgrpc;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.function.Consumer;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CreateSubsArgs;
import com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CreateSubsReply;
import com.cisco.stbarth.netconf.xrgrpc.gRPCConfigOperGrpc.gRPCConfigOperStub;
import io.grpc.ManagedChannel;
import io.grpc.Metadata;
import io.grpc.okhttp.OkHttpChannelBuilder;
import io.grpc.stub.MetadataUtils;
import io.grpc.stub.StreamObserver;

public class XRGRPCClient implements AutoCloseable {
    private ManagedChannel channel;
    private gRPCConfigOperStub stub;
    private long requestID = 0;

    public enum SubscriptionEncoding {
        GPB(2), GPBKV(3), JSON(4);
        private final int value;

        SubscriptionEncoding(int value) {
            this.value = value;
        }

        int getValue() {
            return value;
        }
    }

    public enum XRGRPCClientSecurity {
        PLAINTEXT, TLS, TLS_UNVERIFIED;
    }

    public class XRException extends Exception {
		XRException(String message) {
            super(message);
        }
    }

    public XRGRPCClient(String host, int port, String username, String password, XRGRPCClientSecurity security) {
        OkHttpChannelBuilder channelBuilder = OkHttpChannelBuilder.forAddress(host, port);

        switch (security) {
        case PLAINTEXT:
            channelBuilder.usePlaintext(true);
            break;

        case TLS_UNVERIFIED:
            TrustManager[] trustAllCerts = new TrustManager[] { 
                new X509TrustManager() {     
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {

                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {

                    }

                    public X509Certificate[] getAcceptedIssuers() { 
                        return new X509Certificate[0];
                    }
                }
            }; 
        
            SSLContext sc;
			try {
                sc = SSLContext.getInstance("TLSv1");
                sc.init(null, trustAllCerts, new java.security.SecureRandom()); 
                channelBuilder.sslSocketFactory(sc.getSocketFactory());
			} catch (NoSuchAlgorithmException | KeyManagementException e) {
				e.printStackTrace();
			} 

        case TLS:
            channelBuilder.useTransportSecurity();
            break;
        }

        Metadata headers = new Metadata();
        headers.put(Metadata.Key.of("username", Metadata.ASCII_STRING_MARSHALLER), username);
        headers.put(Metadata.Key.of("password", Metadata.ASCII_STRING_MARSHALLER), password);

        channel = channelBuilder.build();
        stub = MetadataUtils.attachHeaders(gRPCConfigOperGrpc.newStub(channel), headers);
    }

    public void createSubscription(String subscription, SubscriptionEncoding encoding,
            Consumer<byte[]> telemetryConsumer, Consumer<Throwable> terminationConsumer) {
        CreateSubsArgs arg = CreateSubsArgs.newBuilder().setReqId(++requestID)
                .setEncode(encoding.getValue()).setSubidstr(subscription).build();
        StreamObserver<CreateSubsReply> observer = new StreamObserver<CreateSubsReply>() {
            private Throwable terminationException = null;

			@Override
			public void onNext(CreateSubsReply value) {
                if (!value.getData().isEmpty())
                    telemetryConsumer.accept(value.getData().toByteArray());

                if (!value.getErrors().isEmpty())
                    terminationException = new XRException(value.getErrors());
			}

			@Override
			public void onError(Throwable t) {
				terminationConsumer.accept(t);
			}

			@Override
			public void onCompleted() {
				terminationConsumer.accept(terminationException);
			}
        };
        stub.createSubs(arg, observer);
    }

	@Override
	public void close() throws Exception {
		channel.shutdownNow();
    }
}
