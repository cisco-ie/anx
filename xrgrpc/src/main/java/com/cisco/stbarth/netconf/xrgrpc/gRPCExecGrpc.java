package com.cisco.stbarth.netconf.xrgrpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.10.0)",
    comments = "Source: mdt_grpc_dialin.proto")
public final class gRPCExecGrpc {

  private gRPCExecGrpc() {}

  public static final String SERVICE_NAME = "IOSXRExtensibleManagabilityService.gRPCExec";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getShowCmdTextOutputMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdTextReply> METHOD_SHOW_CMD_TEXT_OUTPUT = getShowCmdTextOutputMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdTextReply> getShowCmdTextOutputMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdTextReply> getShowCmdTextOutputMethod() {
    return getShowCmdTextOutputMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdTextReply> getShowCmdTextOutputMethodHelper() {
    io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdArgs, com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdTextReply> getShowCmdTextOutputMethod;
    if ((getShowCmdTextOutputMethod = gRPCExecGrpc.getShowCmdTextOutputMethod) == null) {
      synchronized (gRPCExecGrpc.class) {
        if ((getShowCmdTextOutputMethod = gRPCExecGrpc.getShowCmdTextOutputMethod) == null) {
          gRPCExecGrpc.getShowCmdTextOutputMethod = getShowCmdTextOutputMethod = 
              io.grpc.MethodDescriptor.<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdArgs, com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdTextReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "IOSXRExtensibleManagabilityService.gRPCExec", "ShowCmdTextOutput"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdArgs.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdTextReply.getDefaultInstance()))
                  .setSchemaDescriptor(new gRPCExecMethodDescriptorSupplier("ShowCmdTextOutput"))
                  .build();
          }
        }
     }
     return getShowCmdTextOutputMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getShowCmdJSONOutputMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdJSONReply> METHOD_SHOW_CMD_JSONOUTPUT = getShowCmdJSONOutputMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdJSONReply> getShowCmdJSONOutputMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdJSONReply> getShowCmdJSONOutputMethod() {
    return getShowCmdJSONOutputMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdJSONReply> getShowCmdJSONOutputMethodHelper() {
    io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdArgs, com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdJSONReply> getShowCmdJSONOutputMethod;
    if ((getShowCmdJSONOutputMethod = gRPCExecGrpc.getShowCmdJSONOutputMethod) == null) {
      synchronized (gRPCExecGrpc.class) {
        if ((getShowCmdJSONOutputMethod = gRPCExecGrpc.getShowCmdJSONOutputMethod) == null) {
          gRPCExecGrpc.getShowCmdJSONOutputMethod = getShowCmdJSONOutputMethod = 
              io.grpc.MethodDescriptor.<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdArgs, com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdJSONReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "IOSXRExtensibleManagabilityService.gRPCExec", "ShowCmdJSONOutput"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdArgs.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdJSONReply.getDefaultInstance()))
                  .setSchemaDescriptor(new gRPCExecMethodDescriptorSupplier("ShowCmdJSONOutput"))
                  .build();
          }
        }
     }
     return getShowCmdJSONOutputMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static gRPCExecStub newStub(io.grpc.Channel channel) {
    return new gRPCExecStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static gRPCExecBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new gRPCExecBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static gRPCExecFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new gRPCExecFutureStub(channel);
  }

  /**
   */
  public static abstract class gRPCExecImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Exec commands
     * </pre>
     */
    public void showCmdTextOutput(com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdTextReply> responseObserver) {
      asyncUnimplementedUnaryCall(getShowCmdTextOutputMethodHelper(), responseObserver);
    }

    /**
     */
    public void showCmdJSONOutput(com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdJSONReply> responseObserver) {
      asyncUnimplementedUnaryCall(getShowCmdJSONOutputMethodHelper(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getShowCmdTextOutputMethodHelper(),
            asyncServerStreamingCall(
              new MethodHandlers<
                com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdArgs,
                com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdTextReply>(
                  this, METHODID_SHOW_CMD_TEXT_OUTPUT)))
          .addMethod(
            getShowCmdJSONOutputMethodHelper(),
            asyncServerStreamingCall(
              new MethodHandlers<
                com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdArgs,
                com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdJSONReply>(
                  this, METHODID_SHOW_CMD_JSONOUTPUT)))
          .build();
    }
  }

  /**
   */
  public static final class gRPCExecStub extends io.grpc.stub.AbstractStub<gRPCExecStub> {
    private gRPCExecStub(io.grpc.Channel channel) {
      super(channel);
    }

    private gRPCExecStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected gRPCExecStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new gRPCExecStub(channel, callOptions);
    }

    /**
     * <pre>
     * Exec commands
     * </pre>
     */
    public void showCmdTextOutput(com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdTextReply> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getShowCmdTextOutputMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void showCmdJSONOutput(com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdJSONReply> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getShowCmdJSONOutputMethodHelper(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class gRPCExecBlockingStub extends io.grpc.stub.AbstractStub<gRPCExecBlockingStub> {
    private gRPCExecBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private gRPCExecBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected gRPCExecBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new gRPCExecBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Exec commands
     * </pre>
     */
    public java.util.Iterator<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdTextReply> showCmdTextOutput(
        com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdArgs request) {
      return blockingServerStreamingCall(
          getChannel(), getShowCmdTextOutputMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdJSONReply> showCmdJSONOutput(
        com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdArgs request) {
      return blockingServerStreamingCall(
          getChannel(), getShowCmdJSONOutputMethodHelper(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class gRPCExecFutureStub extends io.grpc.stub.AbstractStub<gRPCExecFutureStub> {
    private gRPCExecFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private gRPCExecFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected gRPCExecFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new gRPCExecFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_SHOW_CMD_TEXT_OUTPUT = 0;
  private static final int METHODID_SHOW_CMD_JSONOUTPUT = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final gRPCExecImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(gRPCExecImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SHOW_CMD_TEXT_OUTPUT:
          serviceImpl.showCmdTextOutput((com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdArgs) request,
              (io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdTextReply>) responseObserver);
          break;
        case METHODID_SHOW_CMD_JSONOUTPUT:
          serviceImpl.showCmdJSONOutput((com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdArgs) request,
              (io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ShowCmdJSONReply>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class gRPCExecBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    gRPCExecBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.cisco.stbarth.netconf.xrgrpc.XRGRPC.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("gRPCExec");
    }
  }

  private static final class gRPCExecFileDescriptorSupplier
      extends gRPCExecBaseDescriptorSupplier {
    gRPCExecFileDescriptorSupplier() {}
  }

  private static final class gRPCExecMethodDescriptorSupplier
      extends gRPCExecBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    gRPCExecMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (gRPCExecGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new gRPCExecFileDescriptorSupplier())
              .addMethod(getShowCmdTextOutputMethodHelper())
              .addMethod(getShowCmdJSONOutputMethodHelper())
              .build();
        }
      }
    }
    return result;
  }
}
