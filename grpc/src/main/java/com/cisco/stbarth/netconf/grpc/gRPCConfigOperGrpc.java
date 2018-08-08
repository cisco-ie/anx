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
public final class gRPCConfigOperGrpc {

  private gRPCConfigOperGrpc() {}

  public static final String SERVICE_NAME = "IOSXRExtensibleManagabilityService.gRPCConfigOper";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigGetArgs,
      com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigGetReply> METHOD_GET_CONFIG =
      io.grpc.MethodDescriptor.<com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigGetArgs, com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigGetReply>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
          .setFullMethodName(generateFullMethodName(
              "IOSXRExtensibleManagabilityService.gRPCConfigOper", "GetConfig"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigGetArgs.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigGetReply.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigArgs,
      com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigReply> METHOD_MERGE_CONFIG =
      io.grpc.MethodDescriptor.<com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigArgs, com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigReply>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "IOSXRExtensibleManagabilityService.gRPCConfigOper", "MergeConfig"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigArgs.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigReply.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigArgs,
      com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigReply> METHOD_DELETE_CONFIG =
      io.grpc.MethodDescriptor.<com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigArgs, com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigReply>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "IOSXRExtensibleManagabilityService.gRPCConfigOper", "DeleteConfig"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigArgs.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigReply.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigArgs,
      com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigReply> METHOD_REPLACE_CONFIG =
      io.grpc.MethodDescriptor.<com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigArgs, com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigReply>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "IOSXRExtensibleManagabilityService.gRPCConfigOper", "ReplaceConfig"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigArgs.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigReply.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.grpc.XRGRPC.CliConfigArgs,
      com.cisco.stbarth.netconf.grpc.XRGRPC.CliConfigReply> METHOD_CLI_CONFIG =
      io.grpc.MethodDescriptor.<com.cisco.stbarth.netconf.grpc.XRGRPC.CliConfigArgs, com.cisco.stbarth.netconf.grpc.XRGRPC.CliConfigReply>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "IOSXRExtensibleManagabilityService.gRPCConfigOper", "CliConfig"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.cisco.stbarth.netconf.grpc.XRGRPC.CliConfigArgs.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.cisco.stbarth.netconf.grpc.XRGRPC.CliConfigReply.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.grpc.XRGRPC.CommitReplaceArgs,
      com.cisco.stbarth.netconf.grpc.XRGRPC.CommitReplaceReply> METHOD_COMMIT_REPLACE =
      io.grpc.MethodDescriptor.<com.cisco.stbarth.netconf.grpc.XRGRPC.CommitReplaceArgs, com.cisco.stbarth.netconf.grpc.XRGRPC.CommitReplaceReply>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "IOSXRExtensibleManagabilityService.gRPCConfigOper", "CommitReplace"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.cisco.stbarth.netconf.grpc.XRGRPC.CommitReplaceArgs.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.cisco.stbarth.netconf.grpc.XRGRPC.CommitReplaceReply.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.grpc.XRGRPC.CommitArgs,
      com.cisco.stbarth.netconf.grpc.XRGRPC.CommitReply> METHOD_COMMIT_CONFIG =
      io.grpc.MethodDescriptor.<com.cisco.stbarth.netconf.grpc.XRGRPC.CommitArgs, com.cisco.stbarth.netconf.grpc.XRGRPC.CommitReply>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "IOSXRExtensibleManagabilityService.gRPCConfigOper", "CommitConfig"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.cisco.stbarth.netconf.grpc.XRGRPC.CommitArgs.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.cisco.stbarth.netconf.grpc.XRGRPC.CommitReply.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.grpc.XRGRPC.DiscardChangesArgs,
      com.cisco.stbarth.netconf.grpc.XRGRPC.DiscardChangesReply> METHOD_CONFIG_DISCARD_CHANGES =
      io.grpc.MethodDescriptor.<com.cisco.stbarth.netconf.grpc.XRGRPC.DiscardChangesArgs, com.cisco.stbarth.netconf.grpc.XRGRPC.DiscardChangesReply>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "IOSXRExtensibleManagabilityService.gRPCConfigOper", "ConfigDiscardChanges"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.cisco.stbarth.netconf.grpc.XRGRPC.DiscardChangesArgs.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.cisco.stbarth.netconf.grpc.XRGRPC.DiscardChangesReply.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.grpc.XRGRPC.GetOperArgs,
      com.cisco.stbarth.netconf.grpc.XRGRPC.GetOperReply> METHOD_GET_OPER =
      io.grpc.MethodDescriptor.<com.cisco.stbarth.netconf.grpc.XRGRPC.GetOperArgs, com.cisco.stbarth.netconf.grpc.XRGRPC.GetOperReply>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
          .setFullMethodName(generateFullMethodName(
              "IOSXRExtensibleManagabilityService.gRPCConfigOper", "GetOper"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.cisco.stbarth.netconf.grpc.XRGRPC.GetOperArgs.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.cisco.stbarth.netconf.grpc.XRGRPC.GetOperReply.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.grpc.XRGRPC.CreateSubsArgs,
      com.cisco.stbarth.netconf.grpc.XRGRPC.CreateSubsReply> METHOD_CREATE_SUBS =
      io.grpc.MethodDescriptor.<com.cisco.stbarth.netconf.grpc.XRGRPC.CreateSubsArgs, com.cisco.stbarth.netconf.grpc.XRGRPC.CreateSubsReply>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
          .setFullMethodName(generateFullMethodName(
              "IOSXRExtensibleManagabilityService.gRPCConfigOper", "CreateSubs"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.cisco.stbarth.netconf.grpc.XRGRPC.CreateSubsArgs.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.cisco.stbarth.netconf.grpc.XRGRPC.CreateSubsReply.getDefaultInstance()))
          .build();

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static gRPCConfigOperStub newStub(io.grpc.Channel channel) {
    return new gRPCConfigOperStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static gRPCConfigOperBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new gRPCConfigOperBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static gRPCConfigOperFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new gRPCConfigOperFutureStub(channel);
  }

  /**
   */
  public static abstract class gRPCConfigOperImplBase implements io.grpc.BindableService {

    /**
     */
    public void getConfig(com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigGetArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigGetReply> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_GET_CONFIG, responseObserver);
    }

    /**
     */
    public void mergeConfig(com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigReply> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_MERGE_CONFIG, responseObserver);
    }

    /**
     */
    public void deleteConfig(com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigReply> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_DELETE_CONFIG, responseObserver);
    }

    /**
     */
    public void replaceConfig(com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigReply> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_REPLACE_CONFIG, responseObserver);
    }

    /**
     */
    public void cliConfig(com.cisco.stbarth.netconf.grpc.XRGRPC.CliConfigArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.grpc.XRGRPC.CliConfigReply> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_CLI_CONFIG, responseObserver);
    }

    /**
     */
    public void commitReplace(com.cisco.stbarth.netconf.grpc.XRGRPC.CommitReplaceArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.grpc.XRGRPC.CommitReplaceReply> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_COMMIT_REPLACE, responseObserver);
    }

    /**
     * <pre>
     * Do we need implicit or explicit commit
     * </pre>
     */
    public void commitConfig(com.cisco.stbarth.netconf.grpc.XRGRPC.CommitArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.grpc.XRGRPC.CommitReply> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_COMMIT_CONFIG, responseObserver);
    }

    /**
     */
    public void configDiscardChanges(com.cisco.stbarth.netconf.grpc.XRGRPC.DiscardChangesArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.grpc.XRGRPC.DiscardChangesReply> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_CONFIG_DISCARD_CHANGES, responseObserver);
    }

    /**
     * <pre>
     * Get only returns oper data
     * 
     * </pre>
     */
    public void getOper(com.cisco.stbarth.netconf.grpc.XRGRPC.GetOperArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.grpc.XRGRPC.GetOperReply> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_GET_OPER, responseObserver);
    }

    /**
     * <pre>
     * Get Telemetry Data
     * </pre>
     */
    public void createSubs(com.cisco.stbarth.netconf.grpc.XRGRPC.CreateSubsArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.grpc.XRGRPC.CreateSubsReply> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_CREATE_SUBS, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_GET_CONFIG,
            asyncServerStreamingCall(
              new MethodHandlers<
                com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigGetArgs,
                com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigGetReply>(
                  this, METHODID_GET_CONFIG)))
          .addMethod(
            METHOD_MERGE_CONFIG,
            asyncUnaryCall(
              new MethodHandlers<
                com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigArgs,
                com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigReply>(
                  this, METHODID_MERGE_CONFIG)))
          .addMethod(
            METHOD_DELETE_CONFIG,
            asyncUnaryCall(
              new MethodHandlers<
                com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigArgs,
                com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigReply>(
                  this, METHODID_DELETE_CONFIG)))
          .addMethod(
            METHOD_REPLACE_CONFIG,
            asyncUnaryCall(
              new MethodHandlers<
                com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigArgs,
                com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigReply>(
                  this, METHODID_REPLACE_CONFIG)))
          .addMethod(
            METHOD_CLI_CONFIG,
            asyncUnaryCall(
              new MethodHandlers<
                com.cisco.stbarth.netconf.grpc.XRGRPC.CliConfigArgs,
                com.cisco.stbarth.netconf.grpc.XRGRPC.CliConfigReply>(
                  this, METHODID_CLI_CONFIG)))
          .addMethod(
            METHOD_COMMIT_REPLACE,
            asyncUnaryCall(
              new MethodHandlers<
                com.cisco.stbarth.netconf.grpc.XRGRPC.CommitReplaceArgs,
                com.cisco.stbarth.netconf.grpc.XRGRPC.CommitReplaceReply>(
                  this, METHODID_COMMIT_REPLACE)))
          .addMethod(
            METHOD_COMMIT_CONFIG,
            asyncUnaryCall(
              new MethodHandlers<
                com.cisco.stbarth.netconf.grpc.XRGRPC.CommitArgs,
                com.cisco.stbarth.netconf.grpc.XRGRPC.CommitReply>(
                  this, METHODID_COMMIT_CONFIG)))
          .addMethod(
            METHOD_CONFIG_DISCARD_CHANGES,
            asyncUnaryCall(
              new MethodHandlers<
                com.cisco.stbarth.netconf.grpc.XRGRPC.DiscardChangesArgs,
                com.cisco.stbarth.netconf.grpc.XRGRPC.DiscardChangesReply>(
                  this, METHODID_CONFIG_DISCARD_CHANGES)))
          .addMethod(
            METHOD_GET_OPER,
            asyncServerStreamingCall(
              new MethodHandlers<
                com.cisco.stbarth.netconf.grpc.XRGRPC.GetOperArgs,
                com.cisco.stbarth.netconf.grpc.XRGRPC.GetOperReply>(
                  this, METHODID_GET_OPER)))
          .addMethod(
            METHOD_CREATE_SUBS,
            asyncServerStreamingCall(
              new MethodHandlers<
                com.cisco.stbarth.netconf.grpc.XRGRPC.CreateSubsArgs,
                com.cisco.stbarth.netconf.grpc.XRGRPC.CreateSubsReply>(
                  this, METHODID_CREATE_SUBS)))
          .build();
    }
  }

  /**
   */
  public static final class gRPCConfigOperStub extends io.grpc.stub.AbstractStub<gRPCConfigOperStub> {
    private gRPCConfigOperStub(io.grpc.Channel channel) {
      super(channel);
    }

    private gRPCConfigOperStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected gRPCConfigOperStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new gRPCConfigOperStub(channel, callOptions);
    }

    /**
     */
    public void getConfig(com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigGetArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigGetReply> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(METHOD_GET_CONFIG, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void mergeConfig(com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_MERGE_CONFIG, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deleteConfig(com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_DELETE_CONFIG, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void replaceConfig(com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_REPLACE_CONFIG, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void cliConfig(com.cisco.stbarth.netconf.grpc.XRGRPC.CliConfigArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.grpc.XRGRPC.CliConfigReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_CLI_CONFIG, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void commitReplace(com.cisco.stbarth.netconf.grpc.XRGRPC.CommitReplaceArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.grpc.XRGRPC.CommitReplaceReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_COMMIT_REPLACE, getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Do we need implicit or explicit commit
     * </pre>
     */
    public void commitConfig(com.cisco.stbarth.netconf.grpc.XRGRPC.CommitArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.grpc.XRGRPC.CommitReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_COMMIT_CONFIG, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void configDiscardChanges(com.cisco.stbarth.netconf.grpc.XRGRPC.DiscardChangesArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.grpc.XRGRPC.DiscardChangesReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_CONFIG_DISCARD_CHANGES, getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Get only returns oper data
     * 
     * </pre>
     */
    public void getOper(com.cisco.stbarth.netconf.grpc.XRGRPC.GetOperArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.grpc.XRGRPC.GetOperReply> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(METHOD_GET_OPER, getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Get Telemetry Data
     * </pre>
     */
    public void createSubs(com.cisco.stbarth.netconf.grpc.XRGRPC.CreateSubsArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.grpc.XRGRPC.CreateSubsReply> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(METHOD_CREATE_SUBS, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class gRPCConfigOperBlockingStub extends io.grpc.stub.AbstractStub<gRPCConfigOperBlockingStub> {
    private gRPCConfigOperBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private gRPCConfigOperBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected gRPCConfigOperBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new gRPCConfigOperBlockingStub(channel, callOptions);
    }

    /**
     */
    public java.util.Iterator<com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigGetReply> getConfig(
        com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigGetArgs request) {
      return blockingServerStreamingCall(
          getChannel(), METHOD_GET_CONFIG, getCallOptions(), request);
    }

    /**
     */
    public com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigReply mergeConfig(com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigArgs request) {
      return blockingUnaryCall(
          getChannel(), METHOD_MERGE_CONFIG, getCallOptions(), request);
    }

    /**
     */
    public com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigReply deleteConfig(com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigArgs request) {
      return blockingUnaryCall(
          getChannel(), METHOD_DELETE_CONFIG, getCallOptions(), request);
    }

    /**
     */
    public com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigReply replaceConfig(com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigArgs request) {
      return blockingUnaryCall(
          getChannel(), METHOD_REPLACE_CONFIG, getCallOptions(), request);
    }

    /**
     */
    public com.cisco.stbarth.netconf.grpc.XRGRPC.CliConfigReply cliConfig(com.cisco.stbarth.netconf.grpc.XRGRPC.CliConfigArgs request) {
      return blockingUnaryCall(
          getChannel(), METHOD_CLI_CONFIG, getCallOptions(), request);
    }

    /**
     */
    public com.cisco.stbarth.netconf.grpc.XRGRPC.CommitReplaceReply commitReplace(com.cisco.stbarth.netconf.grpc.XRGRPC.CommitReplaceArgs request) {
      return blockingUnaryCall(
          getChannel(), METHOD_COMMIT_REPLACE, getCallOptions(), request);
    }

    /**
     * <pre>
     * Do we need implicit or explicit commit
     * </pre>
     */
    public com.cisco.stbarth.netconf.grpc.XRGRPC.CommitReply commitConfig(com.cisco.stbarth.netconf.grpc.XRGRPC.CommitArgs request) {
      return blockingUnaryCall(
          getChannel(), METHOD_COMMIT_CONFIG, getCallOptions(), request);
    }

    /**
     */
    public com.cisco.stbarth.netconf.grpc.XRGRPC.DiscardChangesReply configDiscardChanges(com.cisco.stbarth.netconf.grpc.XRGRPC.DiscardChangesArgs request) {
      return blockingUnaryCall(
          getChannel(), METHOD_CONFIG_DISCARD_CHANGES, getCallOptions(), request);
    }

    /**
     * <pre>
     * Get only returns oper data
     * 
     * </pre>
     */
    public java.util.Iterator<com.cisco.stbarth.netconf.grpc.XRGRPC.GetOperReply> getOper(
        com.cisco.stbarth.netconf.grpc.XRGRPC.GetOperArgs request) {
      return blockingServerStreamingCall(
          getChannel(), METHOD_GET_OPER, getCallOptions(), request);
    }

    /**
     * <pre>
     * Get Telemetry Data
     * </pre>
     */
    public java.util.Iterator<com.cisco.stbarth.netconf.grpc.XRGRPC.CreateSubsReply> createSubs(
        com.cisco.stbarth.netconf.grpc.XRGRPC.CreateSubsArgs request) {
      return blockingServerStreamingCall(
          getChannel(), METHOD_CREATE_SUBS, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class gRPCConfigOperFutureStub extends io.grpc.stub.AbstractStub<gRPCConfigOperFutureStub> {
    private gRPCConfigOperFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private gRPCConfigOperFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected gRPCConfigOperFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new gRPCConfigOperFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigReply> mergeConfig(
        com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigArgs request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_MERGE_CONFIG, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigReply> deleteConfig(
        com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigArgs request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_DELETE_CONFIG, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigReply> replaceConfig(
        com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigArgs request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_REPLACE_CONFIG, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.cisco.stbarth.netconf.grpc.XRGRPC.CliConfigReply> cliConfig(
        com.cisco.stbarth.netconf.grpc.XRGRPC.CliConfigArgs request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_CLI_CONFIG, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.cisco.stbarth.netconf.grpc.XRGRPC.CommitReplaceReply> commitReplace(
        com.cisco.stbarth.netconf.grpc.XRGRPC.CommitReplaceArgs request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_COMMIT_REPLACE, getCallOptions()), request);
    }

    /**
     * <pre>
     * Do we need implicit or explicit commit
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.cisco.stbarth.netconf.grpc.XRGRPC.CommitReply> commitConfig(
        com.cisco.stbarth.netconf.grpc.XRGRPC.CommitArgs request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_COMMIT_CONFIG, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.cisco.stbarth.netconf.grpc.XRGRPC.DiscardChangesReply> configDiscardChanges(
        com.cisco.stbarth.netconf.grpc.XRGRPC.DiscardChangesArgs request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_CONFIG_DISCARD_CHANGES, getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_CONFIG = 0;
  private static final int METHODID_MERGE_CONFIG = 1;
  private static final int METHODID_DELETE_CONFIG = 2;
  private static final int METHODID_REPLACE_CONFIG = 3;
  private static final int METHODID_CLI_CONFIG = 4;
  private static final int METHODID_COMMIT_REPLACE = 5;
  private static final int METHODID_COMMIT_CONFIG = 6;
  private static final int METHODID_CONFIG_DISCARD_CHANGES = 7;
  private static final int METHODID_GET_OPER = 8;
  private static final int METHODID_CREATE_SUBS = 9;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final gRPCConfigOperImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(gRPCConfigOperImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_CONFIG:
          serviceImpl.getConfig((com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigGetArgs) request,
              (io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigGetReply>) responseObserver);
          break;
        case METHODID_MERGE_CONFIG:
          serviceImpl.mergeConfig((com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigArgs) request,
              (io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigReply>) responseObserver);
          break;
        case METHODID_DELETE_CONFIG:
          serviceImpl.deleteConfig((com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigArgs) request,
              (io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigReply>) responseObserver);
          break;
        case METHODID_REPLACE_CONFIG:
          serviceImpl.replaceConfig((com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigArgs) request,
              (io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.grpc.XRGRPC.ConfigReply>) responseObserver);
          break;
        case METHODID_CLI_CONFIG:
          serviceImpl.cliConfig((com.cisco.stbarth.netconf.grpc.XRGRPC.CliConfigArgs) request,
              (io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.grpc.XRGRPC.CliConfigReply>) responseObserver);
          break;
        case METHODID_COMMIT_REPLACE:
          serviceImpl.commitReplace((com.cisco.stbarth.netconf.grpc.XRGRPC.CommitReplaceArgs) request,
              (io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.grpc.XRGRPC.CommitReplaceReply>) responseObserver);
          break;
        case METHODID_COMMIT_CONFIG:
          serviceImpl.commitConfig((com.cisco.stbarth.netconf.grpc.XRGRPC.CommitArgs) request,
              (io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.grpc.XRGRPC.CommitReply>) responseObserver);
          break;
        case METHODID_CONFIG_DISCARD_CHANGES:
          serviceImpl.configDiscardChanges((com.cisco.stbarth.netconf.grpc.XRGRPC.DiscardChangesArgs) request,
              (io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.grpc.XRGRPC.DiscardChangesReply>) responseObserver);
          break;
        case METHODID_GET_OPER:
          serviceImpl.getOper((com.cisco.stbarth.netconf.grpc.XRGRPC.GetOperArgs) request,
              (io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.grpc.XRGRPC.GetOperReply>) responseObserver);
          break;
        case METHODID_CREATE_SUBS:
          serviceImpl.createSubs((com.cisco.stbarth.netconf.grpc.XRGRPC.CreateSubsArgs) request,
              (io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.grpc.XRGRPC.CreateSubsReply>) responseObserver);
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

  private static final class gRPCConfigOperDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.cisco.stbarth.netconf.grpc.XRGRPC.getDescriptor();
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (gRPCConfigOperGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new gRPCConfigOperDescriptorSupplier())
              .addMethod(METHOD_GET_CONFIG)
              .addMethod(METHOD_MERGE_CONFIG)
              .addMethod(METHOD_DELETE_CONFIG)
              .addMethod(METHOD_REPLACE_CONFIG)
              .addMethod(METHOD_CLI_CONFIG)
              .addMethod(METHOD_COMMIT_REPLACE)
              .addMethod(METHOD_COMMIT_CONFIG)
              .addMethod(METHOD_CONFIG_DISCARD_CHANGES)
              .addMethod(METHOD_GET_OPER)
              .addMethod(METHOD_CREATE_SUBS)
              .build();
        }
      }
    }
    return result;
  }
}
