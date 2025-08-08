package com.tdd.rpc;

import com.google.protobuf.Empty;
import com.tdd.app.RequestForm;
import com.tdd.app.ResponseSingle;
import com.tdd.app.Tdd_V1Grpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class TddServiceImpl extends Tdd_V1Grpc.Tdd_V1ImplBase {

    @Override
    public void tLV1(Empty req, StreamObserver<ResponseSingle> responseObserver) {
        String message = "Hello, TLV1";
        ResponseSingle response = ResponseSingle.newBuilder().setMessage(message).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void tLV2(RequestForm req, StreamObserver<ResponseSingle> responseObserver) {
        try {
            for (int i = 1; i <= 10; i++) {
                if (i == 5) { // 模拟一个业务逻辑错误
                    throw new IllegalArgumentException("Invalid step 5");
                }
                String message = String.format("Message %d for request: %s", i, req.getReq());
                ResponseSingle response = ResponseSingle.newBuilder().setMessage(message).build();
                responseObserver.onNext(response);
                Thread.sleep(300); // 模拟延迟
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            responseObserver.onError(
                io.grpc.Status.CANCELLED.withDescription("Request was cancelled").asRuntimeException()
            );
        } catch (Exception e) {
            // 异常将由全局处理器捕获
            throw new RuntimeException(e);
        }
        responseObserver.onCompleted();
    }
}
