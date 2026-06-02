package com.example.devicegateway.grpc;

import com.example.family.grpc.GetParentIdRequest;
import com.example.family.grpc.GetParentIdResponse;
import com.example.family.grpc.IdentityFamilyServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class IdentityFamilyGrpcClient {
    @Value("${grpc.client.host}")
    private String grpcHost;

    @Value("${grpc.client.port}")
    private int grpcPort;

    private final ManagedChannel channel;
    private final IdentityFamilyServiceGrpc.IdentityFamilyServiceStub asyncStub;

    public IdentityFamilyGrpcClient() {
        this.channel = ManagedChannelBuilder
                .forAddress(grpcHost, grpcPort)
                .usePlaintext()
                .build();

        this.asyncStub = IdentityFamilyServiceGrpc.newStub(channel);
    }

    public Mono<String> getParentId(String userId, String child) {
        return Mono.create(sink -> {
            GetParentIdRequest request = GetParentIdRequest.newBuilder()
                    .setUserId(userId)
                    .setChild(child)
                    .build();

            asyncStub.getParentId(request, new StreamObserver<>() {
                @Override
                public void onNext(GetParentIdResponse value) {
                    sink.success(value.getParentId());
                }

                @Override
                public void onError(Throwable t) {
                    sink.error(t);
                }

                @Override
                public void onCompleted() {
                    // nothing
                }
            });
        });
    }

    @PreDestroy
    public void shutdown() {
        channel.shutdown();
    }
}
