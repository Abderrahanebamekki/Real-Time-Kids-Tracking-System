package com.example.devicegateway.grpc;

import com.example.identityfamily.grpc.FamilyServiceGrpc;
import com.example.identityfamily.grpc.ValidateChildRequest;
import com.example.identityfamily.grpc.ValidateChildResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import jakarta.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class FamilyServiceClient {

    private final ManagedChannel channel;
    private final FamilyServiceGrpc.FamilyServiceBlockingStub blockingStub;

    public FamilyServiceClient(@Value("${grpc.identity-family.host:localhost}") String host,
                               @Value("${grpc.identity-family.port:9090}") int port) {
        log.info("Creating gRPC channel to {}:{}", host, port);
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        this.blockingStub = FamilyServiceGrpc.newBlockingStub(channel);
    }

    public Mono<ValidateChildResponse> validateChild(String userId, String childId) {
        return Mono.fromCallable(() -> {
            log.info("Calling gRPC service to validate child {} for user {}", childId, userId);
            
            ValidateChildRequest request = ValidateChildRequest.newBuilder()
                    .setUserId(userId)
                    .setChildId(childId)
                    .build();

            ValidateChildResponse response = blockingStub.validateChild(request);
            
            log.info("Received response: valid={}, parentId={}, message={}", 
                    response.getValid(), response.getParentId(), response.getMessage());
            
            return response;
        })
        .subscribeOn(Schedulers.boundedElastic())
        .onErrorResume(throwable -> {
            log.error("gRPC call failed", throwable);
            return Mono.just(ValidateChildResponse.newBuilder()
                    .setValid(false)
                    .setParentId("")
                    .setMessage("Error calling identity family service: " + throwable.getMessage())
                    .build());
        });
    }

    @PreDestroy
    public void shutdown() {
        try {
            log.info("Shutting down gRPC channel...");
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Interrupted while shutting down gRPC channel", e);
        }
    }
}
