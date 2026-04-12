package com.example.family.client;

import com.example.family.proto.GetParentInfoRequest;
import com.example.family.proto.GetParentInfoResponse;
import com.example.family.proto.ParentInfoServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class ReactiveFamilyGrpcClient {

    private final ParentInfoServiceGrpc.ParentInfoServiceBlockingStub blockingStub;
    private final ManagedChannel channel;

    public ReactiveFamilyGrpcClient() {
        this.channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
        this.blockingStub = ParentInfoServiceGrpc.newBlockingStub(channel);
    }

    public Mono<GetParentInfoResponse> getParentInfo(String userId, String childId) {
        return Mono.fromFuture(() -> {
            GetParentInfoRequest request = GetParentInfoRequest.newBuilder()
                    .setUserId(userId)
                    .setChildId(childId)
                    .build();
            return blockingStub.getParentInfo(request);
        });
    }

    public void shutdown() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }
}
