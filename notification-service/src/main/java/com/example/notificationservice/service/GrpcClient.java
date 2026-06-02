package com.example.notificationservice.service;

import com.example.family.grpc.ChildRequest;
import com.example.family.grpc.ChildResponse;
import com.example.family.grpc.IdentityFamilyServiceGrpc;
import com.example.family.grpc.UserResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GrpcClient {


    private final ManagedChannel channel;
    private final IdentityFamilyServiceGrpc.IdentityFamilyServiceStub asyncStub;

    public GrpcClient(
            @Value("${grpc.client.host}") String host,
            @Value("${grpc.client.port}") int port
    ) {
        this.channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
        this.asyncStub = IdentityFamilyServiceGrpc.newStub(channel);
    }

    public Flux<Long> getUsersId(Long childId) {
        return Flux.create(sink -> {
            ChildRequest request = ChildRequest.newBuilder()
                    .setChildId(childId)
                    .build();

            asyncStub.getUsersIdByChild(request, new StreamObserver<UserResponse>() {
                @Override
                public void onNext(UserResponse value) {
                    value.getUsersList().forEach(sink::next);
                }

                @Override
                public void onError(Throwable t) {
                    sink.error(t);
                }

                @Override
                public void onCompleted() {
                    sink.complete();
                }
            });
        });
    }
    public Mono<String> getChildName(Long childId) {
        return Mono.create(sink -> {
            ChildRequest request = ChildRequest.newBuilder()
                    .setChildId(childId)
                    .build();

            asyncStub.getChildName(request, new StreamObserver<>() {
                @Override
                public void onNext(ChildResponse value) {
                    sink.success(value.getChildName());
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
