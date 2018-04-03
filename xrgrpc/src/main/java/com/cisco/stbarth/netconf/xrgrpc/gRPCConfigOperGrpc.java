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
public final class gRPCConfigOperGrpc {

  private gRPCConfigOperGrpc() {}

  public static final String SERVICE_NAME = "IOSXRExtensibleManagabilityService.gRPCConfigOper";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getGetConfigMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigGetArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigGetReply> METHOD_GET_CONFIG = getGetConfigMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigGetArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigGetReply> getGetConfigMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigGetArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigGetReply> getGetConfigMethod() {
    return getGetConfigMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigGetArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigGetReply> getGetConfigMethodHelper() {
    io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigGetArgs, com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigGetReply> getGetConfigMethod;
    if ((getGetConfigMethod = gRPCConfigOperGrpc.getGetConfigMethod) == null) {
      synchronized (gRPCConfigOperGrpc.class) {
        if ((getGetConfigMethod = gRPCConfigOperGrpc.getGetConfigMethod) == null) {
          gRPCConfigOperGrpc.getGetConfigMethod = getGetConfigMethod = 
              io.grpc.MethodDescriptor.<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigGetArgs, com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigGetReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "IOSXRExtensibleManagabilityService.gRPCConfigOper", "GetConfig"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigGetArgs.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigGetReply.getDefaultInstance()))
                  .setSchemaDescriptor(new gRPCConfigOperMethodDescriptorSupplier("GetConfig"))
                  .build();
          }
        }
     }
     return getGetConfigMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getMergeConfigMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigReply> METHOD_MERGE_CONFIG = getMergeConfigMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigReply> getMergeConfigMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigReply> getMergeConfigMethod() {
    return getMergeConfigMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigReply> getMergeConfigMethodHelper() {
    io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigArgs, com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigReply> getMergeConfigMethod;
    if ((getMergeConfigMethod = gRPCConfigOperGrpc.getMergeConfigMethod) == null) {
      synchronized (gRPCConfigOperGrpc.class) {
        if ((getMergeConfigMethod = gRPCConfigOperGrpc.getMergeConfigMethod) == null) {
          gRPCConfigOperGrpc.getMergeConfigMethod = getMergeConfigMethod = 
              io.grpc.MethodDescriptor.<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigArgs, com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "IOSXRExtensibleManagabilityService.gRPCConfigOper", "MergeConfig"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigArgs.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigReply.getDefaultInstance()))
                  .setSchemaDescriptor(new gRPCConfigOperMethodDescriptorSupplier("MergeConfig"))
                  .build();
          }
        }
     }
     return getMergeConfigMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getDeleteConfigMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigReply> METHOD_DELETE_CONFIG = getDeleteConfigMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigReply> getDeleteConfigMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigReply> getDeleteConfigMethod() {
    return getDeleteConfigMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigReply> getDeleteConfigMethodHelper() {
    io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigArgs, com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigReply> getDeleteConfigMethod;
    if ((getDeleteConfigMethod = gRPCConfigOperGrpc.getDeleteConfigMethod) == null) {
      synchronized (gRPCConfigOperGrpc.class) {
        if ((getDeleteConfigMethod = gRPCConfigOperGrpc.getDeleteConfigMethod) == null) {
          gRPCConfigOperGrpc.getDeleteConfigMethod = getDeleteConfigMethod = 
              io.grpc.MethodDescriptor.<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigArgs, com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "IOSXRExtensibleManagabilityService.gRPCConfigOper", "DeleteConfig"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigArgs.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigReply.getDefaultInstance()))
                  .setSchemaDescriptor(new gRPCConfigOperMethodDescriptorSupplier("DeleteConfig"))
                  .build();
          }
        }
     }
     return getDeleteConfigMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getReplaceConfigMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigReply> METHOD_REPLACE_CONFIG = getReplaceConfigMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigReply> getReplaceConfigMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigReply> getReplaceConfigMethod() {
    return getReplaceConfigMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigReply> getReplaceConfigMethodHelper() {
    io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigArgs, com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigReply> getReplaceConfigMethod;
    if ((getReplaceConfigMethod = gRPCConfigOperGrpc.getReplaceConfigMethod) == null) {
      synchronized (gRPCConfigOperGrpc.class) {
        if ((getReplaceConfigMethod = gRPCConfigOperGrpc.getReplaceConfigMethod) == null) {
          gRPCConfigOperGrpc.getReplaceConfigMethod = getReplaceConfigMethod = 
              io.grpc.MethodDescriptor.<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigArgs, com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "IOSXRExtensibleManagabilityService.gRPCConfigOper", "ReplaceConfig"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigArgs.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigReply.getDefaultInstance()))
                  .setSchemaDescriptor(new gRPCConfigOperMethodDescriptorSupplier("ReplaceConfig"))
                  .build();
          }
        }
     }
     return getReplaceConfigMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getCliConfigMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CliConfigArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CliConfigReply> METHOD_CLI_CONFIG = getCliConfigMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CliConfigArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CliConfigReply> getCliConfigMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CliConfigArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CliConfigReply> getCliConfigMethod() {
    return getCliConfigMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CliConfigArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CliConfigReply> getCliConfigMethodHelper() {
    io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CliConfigArgs, com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CliConfigReply> getCliConfigMethod;
    if ((getCliConfigMethod = gRPCConfigOperGrpc.getCliConfigMethod) == null) {
      synchronized (gRPCConfigOperGrpc.class) {
        if ((getCliConfigMethod = gRPCConfigOperGrpc.getCliConfigMethod) == null) {
          gRPCConfigOperGrpc.getCliConfigMethod = getCliConfigMethod = 
              io.grpc.MethodDescriptor.<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CliConfigArgs, com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CliConfigReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "IOSXRExtensibleManagabilityService.gRPCConfigOper", "CliConfig"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CliConfigArgs.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CliConfigReply.getDefaultInstance()))
                  .setSchemaDescriptor(new gRPCConfigOperMethodDescriptorSupplier("CliConfig"))
                  .build();
          }
        }
     }
     return getCliConfigMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getCommitReplaceMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitReplaceArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitReplaceReply> METHOD_COMMIT_REPLACE = getCommitReplaceMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitReplaceArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitReplaceReply> getCommitReplaceMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitReplaceArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitReplaceReply> getCommitReplaceMethod() {
    return getCommitReplaceMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitReplaceArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitReplaceReply> getCommitReplaceMethodHelper() {
    io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitReplaceArgs, com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitReplaceReply> getCommitReplaceMethod;
    if ((getCommitReplaceMethod = gRPCConfigOperGrpc.getCommitReplaceMethod) == null) {
      synchronized (gRPCConfigOperGrpc.class) {
        if ((getCommitReplaceMethod = gRPCConfigOperGrpc.getCommitReplaceMethod) == null) {
          gRPCConfigOperGrpc.getCommitReplaceMethod = getCommitReplaceMethod = 
              io.grpc.MethodDescriptor.<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitReplaceArgs, com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitReplaceReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "IOSXRExtensibleManagabilityService.gRPCConfigOper", "CommitReplace"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitReplaceArgs.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitReplaceReply.getDefaultInstance()))
                  .setSchemaDescriptor(new gRPCConfigOperMethodDescriptorSupplier("CommitReplace"))
                  .build();
          }
        }
     }
     return getCommitReplaceMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getCommitConfigMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitReply> METHOD_COMMIT_CONFIG = getCommitConfigMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitReply> getCommitConfigMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitReply> getCommitConfigMethod() {
    return getCommitConfigMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitReply> getCommitConfigMethodHelper() {
    io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitArgs, com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitReply> getCommitConfigMethod;
    if ((getCommitConfigMethod = gRPCConfigOperGrpc.getCommitConfigMethod) == null) {
      synchronized (gRPCConfigOperGrpc.class) {
        if ((getCommitConfigMethod = gRPCConfigOperGrpc.getCommitConfigMethod) == null) {
          gRPCConfigOperGrpc.getCommitConfigMethod = getCommitConfigMethod = 
              io.grpc.MethodDescriptor.<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitArgs, com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "IOSXRExtensibleManagabilityService.gRPCConfigOper", "CommitConfig"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitArgs.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitReply.getDefaultInstance()))
                  .setSchemaDescriptor(new gRPCConfigOperMethodDescriptorSupplier("CommitConfig"))
                  .build();
          }
        }
     }
     return getCommitConfigMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getConfigDiscardChangesMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.DiscardChangesArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.DiscardChangesReply> METHOD_CONFIG_DISCARD_CHANGES = getConfigDiscardChangesMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.DiscardChangesArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.DiscardChangesReply> getConfigDiscardChangesMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.DiscardChangesArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.DiscardChangesReply> getConfigDiscardChangesMethod() {
    return getConfigDiscardChangesMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.DiscardChangesArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.DiscardChangesReply> getConfigDiscardChangesMethodHelper() {
    io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.DiscardChangesArgs, com.cisco.stbarth.netconf.xrgrpc.XRGRPC.DiscardChangesReply> getConfigDiscardChangesMethod;
    if ((getConfigDiscardChangesMethod = gRPCConfigOperGrpc.getConfigDiscardChangesMethod) == null) {
      synchronized (gRPCConfigOperGrpc.class) {
        if ((getConfigDiscardChangesMethod = gRPCConfigOperGrpc.getConfigDiscardChangesMethod) == null) {
          gRPCConfigOperGrpc.getConfigDiscardChangesMethod = getConfigDiscardChangesMethod = 
              io.grpc.MethodDescriptor.<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.DiscardChangesArgs, com.cisco.stbarth.netconf.xrgrpc.XRGRPC.DiscardChangesReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "IOSXRExtensibleManagabilityService.gRPCConfigOper", "ConfigDiscardChanges"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cisco.stbarth.netconf.xrgrpc.XRGRPC.DiscardChangesArgs.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cisco.stbarth.netconf.xrgrpc.XRGRPC.DiscardChangesReply.getDefaultInstance()))
                  .setSchemaDescriptor(new gRPCConfigOperMethodDescriptorSupplier("ConfigDiscardChanges"))
                  .build();
          }
        }
     }
     return getConfigDiscardChangesMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getGetOperMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.GetOperArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.GetOperReply> METHOD_GET_OPER = getGetOperMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.GetOperArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.GetOperReply> getGetOperMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.GetOperArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.GetOperReply> getGetOperMethod() {
    return getGetOperMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.GetOperArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.GetOperReply> getGetOperMethodHelper() {
    io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.GetOperArgs, com.cisco.stbarth.netconf.xrgrpc.XRGRPC.GetOperReply> getGetOperMethod;
    if ((getGetOperMethod = gRPCConfigOperGrpc.getGetOperMethod) == null) {
      synchronized (gRPCConfigOperGrpc.class) {
        if ((getGetOperMethod = gRPCConfigOperGrpc.getGetOperMethod) == null) {
          gRPCConfigOperGrpc.getGetOperMethod = getGetOperMethod = 
              io.grpc.MethodDescriptor.<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.GetOperArgs, com.cisco.stbarth.netconf.xrgrpc.XRGRPC.GetOperReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "IOSXRExtensibleManagabilityService.gRPCConfigOper", "GetOper"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cisco.stbarth.netconf.xrgrpc.XRGRPC.GetOperArgs.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cisco.stbarth.netconf.xrgrpc.XRGRPC.GetOperReply.getDefaultInstance()))
                  .setSchemaDescriptor(new gRPCConfigOperMethodDescriptorSupplier("GetOper"))
                  .build();
          }
        }
     }
     return getGetOperMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getCreateSubsMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CreateSubsArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CreateSubsReply> METHOD_CREATE_SUBS = getCreateSubsMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CreateSubsArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CreateSubsReply> getCreateSubsMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CreateSubsArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CreateSubsReply> getCreateSubsMethod() {
    return getCreateSubsMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CreateSubsArgs,
      com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CreateSubsReply> getCreateSubsMethodHelper() {
    io.grpc.MethodDescriptor<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CreateSubsArgs, com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CreateSubsReply> getCreateSubsMethod;
    if ((getCreateSubsMethod = gRPCConfigOperGrpc.getCreateSubsMethod) == null) {
      synchronized (gRPCConfigOperGrpc.class) {
        if ((getCreateSubsMethod = gRPCConfigOperGrpc.getCreateSubsMethod) == null) {
          gRPCConfigOperGrpc.getCreateSubsMethod = getCreateSubsMethod = 
              io.grpc.MethodDescriptor.<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CreateSubsArgs, com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CreateSubsReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "IOSXRExtensibleManagabilityService.gRPCConfigOper", "CreateSubs"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CreateSubsArgs.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CreateSubsReply.getDefaultInstance()))
                  .setSchemaDescriptor(new gRPCConfigOperMethodDescriptorSupplier("CreateSubs"))
                  .build();
          }
        }
     }
     return getCreateSubsMethod;
  }

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
    public void getConfig(com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigGetArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigGetReply> responseObserver) {
      asyncUnimplementedUnaryCall(getGetConfigMethodHelper(), responseObserver);
    }

    /**
     */
    public void mergeConfig(com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigReply> responseObserver) {
      asyncUnimplementedUnaryCall(getMergeConfigMethodHelper(), responseObserver);
    }

    /**
     */
    public void deleteConfig(com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigReply> responseObserver) {
      asyncUnimplementedUnaryCall(getDeleteConfigMethodHelper(), responseObserver);
    }

    /**
     */
    public void replaceConfig(com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigReply> responseObserver) {
      asyncUnimplementedUnaryCall(getReplaceConfigMethodHelper(), responseObserver);
    }

    /**
     */
    public void cliConfig(com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CliConfigArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CliConfigReply> responseObserver) {
      asyncUnimplementedUnaryCall(getCliConfigMethodHelper(), responseObserver);
    }

    /**
     */
    public void commitReplace(com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitReplaceArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitReplaceReply> responseObserver) {
      asyncUnimplementedUnaryCall(getCommitReplaceMethodHelper(), responseObserver);
    }

    /**
     * <pre>
     * Do we need implicit or explicit commit
     * </pre>
     */
    public void commitConfig(com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitReply> responseObserver) {
      asyncUnimplementedUnaryCall(getCommitConfigMethodHelper(), responseObserver);
    }

    /**
     */
    public void configDiscardChanges(com.cisco.stbarth.netconf.xrgrpc.XRGRPC.DiscardChangesArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.DiscardChangesReply> responseObserver) {
      asyncUnimplementedUnaryCall(getConfigDiscardChangesMethodHelper(), responseObserver);
    }

    /**
     * <pre>
     * Get only returns oper data
     * 
     * </pre>
     */
    public void getOper(com.cisco.stbarth.netconf.xrgrpc.XRGRPC.GetOperArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.GetOperReply> responseObserver) {
      asyncUnimplementedUnaryCall(getGetOperMethodHelper(), responseObserver);
    }

    /**
     * <pre>
     * Get Telemetry Data
     * </pre>
     */
    public void createSubs(com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CreateSubsArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CreateSubsReply> responseObserver) {
      asyncUnimplementedUnaryCall(getCreateSubsMethodHelper(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetConfigMethodHelper(),
            asyncServerStreamingCall(
              new MethodHandlers<
                com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigGetArgs,
                com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigGetReply>(
                  this, METHODID_GET_CONFIG)))
          .addMethod(
            getMergeConfigMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigArgs,
                com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigReply>(
                  this, METHODID_MERGE_CONFIG)))
          .addMethod(
            getDeleteConfigMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigArgs,
                com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigReply>(
                  this, METHODID_DELETE_CONFIG)))
          .addMethod(
            getReplaceConfigMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigArgs,
                com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigReply>(
                  this, METHODID_REPLACE_CONFIG)))
          .addMethod(
            getCliConfigMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CliConfigArgs,
                com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CliConfigReply>(
                  this, METHODID_CLI_CONFIG)))
          .addMethod(
            getCommitReplaceMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitReplaceArgs,
                com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitReplaceReply>(
                  this, METHODID_COMMIT_REPLACE)))
          .addMethod(
            getCommitConfigMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitArgs,
                com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitReply>(
                  this, METHODID_COMMIT_CONFIG)))
          .addMethod(
            getConfigDiscardChangesMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.cisco.stbarth.netconf.xrgrpc.XRGRPC.DiscardChangesArgs,
                com.cisco.stbarth.netconf.xrgrpc.XRGRPC.DiscardChangesReply>(
                  this, METHODID_CONFIG_DISCARD_CHANGES)))
          .addMethod(
            getGetOperMethodHelper(),
            asyncServerStreamingCall(
              new MethodHandlers<
                com.cisco.stbarth.netconf.xrgrpc.XRGRPC.GetOperArgs,
                com.cisco.stbarth.netconf.xrgrpc.XRGRPC.GetOperReply>(
                  this, METHODID_GET_OPER)))
          .addMethod(
            getCreateSubsMethodHelper(),
            asyncServerStreamingCall(
              new MethodHandlers<
                com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CreateSubsArgs,
                com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CreateSubsReply>(
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
    public void getConfig(com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigGetArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigGetReply> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getGetConfigMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void mergeConfig(com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getMergeConfigMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deleteConfig(com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getDeleteConfigMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void replaceConfig(com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getReplaceConfigMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void cliConfig(com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CliConfigArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CliConfigReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCliConfigMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void commitReplace(com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitReplaceArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitReplaceReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCommitReplaceMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Do we need implicit or explicit commit
     * </pre>
     */
    public void commitConfig(com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCommitConfigMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void configDiscardChanges(com.cisco.stbarth.netconf.xrgrpc.XRGRPC.DiscardChangesArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.DiscardChangesReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getConfigDiscardChangesMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Get only returns oper data
     * 
     * </pre>
     */
    public void getOper(com.cisco.stbarth.netconf.xrgrpc.XRGRPC.GetOperArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.GetOperReply> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getGetOperMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Get Telemetry Data
     * </pre>
     */
    public void createSubs(com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CreateSubsArgs request,
        io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CreateSubsReply> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getCreateSubsMethodHelper(), getCallOptions()), request, responseObserver);
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
    public java.util.Iterator<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigGetReply> getConfig(
        com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigGetArgs request) {
      return blockingServerStreamingCall(
          getChannel(), getGetConfigMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigReply mergeConfig(com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigArgs request) {
      return blockingUnaryCall(
          getChannel(), getMergeConfigMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigReply deleteConfig(com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigArgs request) {
      return blockingUnaryCall(
          getChannel(), getDeleteConfigMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigReply replaceConfig(com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigArgs request) {
      return blockingUnaryCall(
          getChannel(), getReplaceConfigMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CliConfigReply cliConfig(com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CliConfigArgs request) {
      return blockingUnaryCall(
          getChannel(), getCliConfigMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitReplaceReply commitReplace(com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitReplaceArgs request) {
      return blockingUnaryCall(
          getChannel(), getCommitReplaceMethodHelper(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Do we need implicit or explicit commit
     * </pre>
     */
    public com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitReply commitConfig(com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitArgs request) {
      return blockingUnaryCall(
          getChannel(), getCommitConfigMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.cisco.stbarth.netconf.xrgrpc.XRGRPC.DiscardChangesReply configDiscardChanges(com.cisco.stbarth.netconf.xrgrpc.XRGRPC.DiscardChangesArgs request) {
      return blockingUnaryCall(
          getChannel(), getConfigDiscardChangesMethodHelper(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Get only returns oper data
     * 
     * </pre>
     */
    public java.util.Iterator<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.GetOperReply> getOper(
        com.cisco.stbarth.netconf.xrgrpc.XRGRPC.GetOperArgs request) {
      return blockingServerStreamingCall(
          getChannel(), getGetOperMethodHelper(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Get Telemetry Data
     * </pre>
     */
    public java.util.Iterator<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CreateSubsReply> createSubs(
        com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CreateSubsArgs request) {
      return blockingServerStreamingCall(
          getChannel(), getCreateSubsMethodHelper(), getCallOptions(), request);
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
    public com.google.common.util.concurrent.ListenableFuture<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigReply> mergeConfig(
        com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigArgs request) {
      return futureUnaryCall(
          getChannel().newCall(getMergeConfigMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigReply> deleteConfig(
        com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigArgs request) {
      return futureUnaryCall(
          getChannel().newCall(getDeleteConfigMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigReply> replaceConfig(
        com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigArgs request) {
      return futureUnaryCall(
          getChannel().newCall(getReplaceConfigMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CliConfigReply> cliConfig(
        com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CliConfigArgs request) {
      return futureUnaryCall(
          getChannel().newCall(getCliConfigMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitReplaceReply> commitReplace(
        com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitReplaceArgs request) {
      return futureUnaryCall(
          getChannel().newCall(getCommitReplaceMethodHelper(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Do we need implicit or explicit commit
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitReply> commitConfig(
        com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitArgs request) {
      return futureUnaryCall(
          getChannel().newCall(getCommitConfigMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.DiscardChangesReply> configDiscardChanges(
        com.cisco.stbarth.netconf.xrgrpc.XRGRPC.DiscardChangesArgs request) {
      return futureUnaryCall(
          getChannel().newCall(getConfigDiscardChangesMethodHelper(), getCallOptions()), request);
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
          serviceImpl.getConfig((com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigGetArgs) request,
              (io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigGetReply>) responseObserver);
          break;
        case METHODID_MERGE_CONFIG:
          serviceImpl.mergeConfig((com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigArgs) request,
              (io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigReply>) responseObserver);
          break;
        case METHODID_DELETE_CONFIG:
          serviceImpl.deleteConfig((com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigArgs) request,
              (io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigReply>) responseObserver);
          break;
        case METHODID_REPLACE_CONFIG:
          serviceImpl.replaceConfig((com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigArgs) request,
              (io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.ConfigReply>) responseObserver);
          break;
        case METHODID_CLI_CONFIG:
          serviceImpl.cliConfig((com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CliConfigArgs) request,
              (io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CliConfigReply>) responseObserver);
          break;
        case METHODID_COMMIT_REPLACE:
          serviceImpl.commitReplace((com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitReplaceArgs) request,
              (io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitReplaceReply>) responseObserver);
          break;
        case METHODID_COMMIT_CONFIG:
          serviceImpl.commitConfig((com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitArgs) request,
              (io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CommitReply>) responseObserver);
          break;
        case METHODID_CONFIG_DISCARD_CHANGES:
          serviceImpl.configDiscardChanges((com.cisco.stbarth.netconf.xrgrpc.XRGRPC.DiscardChangesArgs) request,
              (io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.DiscardChangesReply>) responseObserver);
          break;
        case METHODID_GET_OPER:
          serviceImpl.getOper((com.cisco.stbarth.netconf.xrgrpc.XRGRPC.GetOperArgs) request,
              (io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.GetOperReply>) responseObserver);
          break;
        case METHODID_CREATE_SUBS:
          serviceImpl.createSubs((com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CreateSubsArgs) request,
              (io.grpc.stub.StreamObserver<com.cisco.stbarth.netconf.xrgrpc.XRGRPC.CreateSubsReply>) responseObserver);
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

  private static abstract class gRPCConfigOperBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    gRPCConfigOperBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.cisco.stbarth.netconf.xrgrpc.XRGRPC.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("gRPCConfigOper");
    }
  }

  private static final class gRPCConfigOperFileDescriptorSupplier
      extends gRPCConfigOperBaseDescriptorSupplier {
    gRPCConfigOperFileDescriptorSupplier() {}
  }

  private static final class gRPCConfigOperMethodDescriptorSupplier
      extends gRPCConfigOperBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    gRPCConfigOperMethodDescriptorSupplier(String methodName) {
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
      synchronized (gRPCConfigOperGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new gRPCConfigOperFileDescriptorSupplier())
              .addMethod(getGetConfigMethodHelper())
              .addMethod(getMergeConfigMethodHelper())
              .addMethod(getDeleteConfigMethodHelper())
              .addMethod(getReplaceConfigMethodHelper())
              .addMethod(getCliConfigMethodHelper())
              .addMethod(getCommitReplaceMethodHelper())
              .addMethod(getCommitConfigMethodHelper())
              .addMethod(getConfigDiscardChangesMethodHelper())
              .addMethod(getGetOperMethodHelper())
              .addMethod(getCreateSubsMethodHelper())
              .build();
        }
      }
    }
    return result;
  }
}
