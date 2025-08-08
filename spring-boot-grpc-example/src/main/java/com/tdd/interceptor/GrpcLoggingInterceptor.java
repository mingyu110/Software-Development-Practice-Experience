package com.tdd.interceptor;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GrpcLoggingInterceptor implements ServerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(GrpcLoggingInterceptor.class);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        Metadata.Key<String> authKey = Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER);
        String authToken = headers.get(authKey);

        logger.info(
            "Received gRPC call. Method: {}, Auth Token: {}, Headers: {}",
            call.getMethodDescriptor().getFullMethodName(),
            authToken != null ? authToken : "[not provided]",
            headers
        );

        return next.startCall(call, headers);
    }
}
