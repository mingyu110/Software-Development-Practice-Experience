package com.tdd.exception;

import io.grpc.Status;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcAdvice
public class GlobalGrpcExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalGrpcExceptionHandler.class);

    @GrpcExceptionHandler(Exception.class)
    public Status handleGeneralException(Exception e) {
        logger.error("gRPC service threw an unexpected exception", e);
        return Status.INTERNAL
                .withDescription("Internal Server Error: " + e.getMessage())
                .withCause(e);
    }

    @GrpcExceptionHandler(IllegalArgumentException.class)
    public Status handleIllegalArgumentException(IllegalArgumentException e) {
        logger.warn("gRPC service received an invalid argument", e);
        return Status.INVALID_ARGUMENT
                .withDescription(e.getMessage())
                .withCause(e);
    }
}
