package com.cisco.stbarth.netconf.grpc;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler",
    comments = "Source: mdt_grpc_dialin.proto")
public final class gRPCExecGrpc {

  private gRPCExecGrpc() {}

  public static final String SERVICE_NAME = "IOSXRExtensibleManagabilityService.gRPCExec";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.grpc.XRGRPC.ShowCmdArgs,
      com.cisco.stbarth.netconf.grpc.XRGRPC.ShowCmdTextReply> METHOD_SHOW_CMD_TEXT_OUTPUT =
      io.grpc.MethodDescriptor.<com.cisco.stbarth.netconf.grpc.XRGRPC.ShowCmdArgs, com.cisco.stbarth.netconf.grpc.XRGRPC.ShowCmdTextReply>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
          .setFullMethodName(generateFullMethodName(
              "IOSXRExtensibleManagabilityService.gRPCExec", "ShowCmdTextOutput"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.cisco.stbarth.netconf.grpc.XRGRPC.ShowCmdArgs.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.cisco.stbarth.netconf.grpc.XRGRPC.ShowCmdTextReply.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.grpc.XRGRPC.ShowCmdArgs,
      com.cisco.stbarth.netconf.grpc.XRGRPC.ShowCmdJSONReply> METHOD_SHOW_CMD_JSONOUTPUT =
      io.grpc.MethodDescriptor.<com.cisco.stbarth.netconf.grpc.XRGRPC.ShowCmdArgs, com.cisco.stbarth.netconf.grpc.XRGRPC.ShowCmdJSONReply>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
          .setFullMethodName(generateFullMethodName(
              "IOSXRExtensibleManagabilityService.gRPCExec", "ShowCmdJSONOutput"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.cisco.stbarth.netconf.grpc.XRGRPC.ShowCmdArgs.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.cisco.stbarth.netconf.grpc.XRGRPC.ShowCmdJSONReply.getDefaultInstance()))
          .build();

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
    public void showCmdTextOutput(com.cisco.stbarth.netconf.grpc.XRGRPC.ShowCmdArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.grpc.XRGRPC.ShowCmdTextReply> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_SHOW_CMD_TEXT_OUTPUT, responseObserver);
    }

    /**
     */
    public void showCmdJSONOutput(com.cisco.stbarth.netconf.grpc.XRGRPC.ShowCmdArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.grpc.XRGRPC.ShowCmdJSONReply> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_SHOW_CMD_JSONOUTPUT, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_SHOW_CMD_TEXT_OUTPUT,
            asyncServerStreamingCall(
              new MethodHandlers<
                com.cisco.stbarth.netconf.grpc.XRGRPC.ShowCmdArgs,
                com.cisco.stbarth.netconf.grpc.XRGRPC.ShowCmdTextReply>(
                  this, METHODID_SHOW_CMD_TEXT_OUTPUT)))
          .addMethod(
            METHOD_SHOW_CMD_JSONOUTPUT,
            asyncServerStreamingCall(
              new MethodHandlers<
                com.cisco.stbarth.netconf.grpc.XRGRPC.ShowCmdArgs,
                com.cisco.stbarth.netconf.grpc.XRGRPC.ShowCmdJSONReply>(
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
    public void showCmdTextOutput(com.cisco.stbarth.netconf.grpc.XRGRPC.ShowCmdArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.grpc.XRGRPC.ShowCmdTextReply> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(METHOD_SHOW_CMD_TEXT_OUTPUT, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void showCmdJSONOutput(com.cisco.stbarth.netconf.grpc.XRGRPC.ShowCmdArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.grpc.XRGRPC.ShowCmdJSONReply> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(METHOD_SHOW_CMD_JSONOUTPUT, getCallOptions()), request, responseObserver);
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
    public java.util.Iterator<com.cisco.stbarth.netconf.grpc.XRGRPC.ShowCmdTextReply> showCmdTextOutput(
        com.cisco.stbarth.netconf.grpc.XRGRPC.ShowCmdArgs request) {
      return blockingServerStreamingCall(
          getChannel(), METHOD_SHOW_CMD_TEXT_OUTPUT, getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<com.cisco.stbarth.netconf.grpc.XRGRPC.ShowCmdJSONReply> showCmdJSONOutput(
        com.cisco.stbarth.netconf.grpc.XRGRPC.ShowCmdArgs request) {
      return blockingServerStreamingCall(
          getChannel(), METHOD_SHOW_CMD_JSONOUTPUT, getCallOptions(), request);
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
          serviceImpl.showCmdTextOutput((com.cisco.stbarth.netconf.grpc.XRGRPC.ShowCmdArgs) request,
              (io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.grpc.XRGRPC.ShowCmdTextReply>) responseObserver);
          break;
        case METHODID_SHOW_CMD_JSONOUTPUT:
          serviceImpl.showCmdJSONOutput((com.cisco.stbarth.netconf.grpc.XRGRPC.ShowCmdArgs) request,
              (io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.grpc.XRGRPC.ShowCmdJSONReply>) responseObserver);
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

  private static final class gRPCExecDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.cisco.stbarth.netconf.grpc.XRGRPC.getDescriptor();
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
              .setSchemaDescriptor(new gRPCExecDescriptorSupplier())
              .addMethod(METHOD_SHOW_CMD_TEXT_OUTPUT)
              .addMethod(METHOD_SHOW_CMD_JSONOUTPUT)
              .build();
        }
      }
    }
    return result;
  }
}
