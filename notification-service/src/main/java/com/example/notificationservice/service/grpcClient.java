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
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class grpcClient {

    private final ManagedChannel channel;
    private final IdentityFamilyServiceGrpc.IdentityFamilyServiceStub asyncStub;

    public grpcClient() {
        this.channel = ManagedChannelBuilder
                .forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        this.asyncStub = IdentityFamilyServiceGrpc.newStub(channel);
    }

    public Mono<List<Long>> getUsersId(Long childId) {
        return Mono.create(sink -> {
            ChildRequest request = ChildRequest.newBuilder()
                    .setChildId(childId)
                    .build();

            asyncStub.getUsersIdByChild(request, new StreamObserver<>() {
                @Override
                public void onNext(UserResponse value) {
                    sink.success(value.getUsersList());
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
