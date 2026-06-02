package com.example.geofencingservice.grpc;

import com.example.device.grpc.ChildResponse;
import com.example.device.grpc.DeviceRequest;
import com.example.device.grpc.DeviceServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class DeviceGrpcClient {

    @Value("${grpc.client.host}")
    private String grpcHost;

    @Value("${grpc.client.port}")
    private int grpcPort;

    private final ManagedChannel channel;
    private final DeviceServiceGrpc.DeviceServiceStub asyncStub;

    public DeviceGrpcClient() {
        this.channel = ManagedChannelBuilder
                .forAddress(grpcHost, grpcPort)
                .usePlaintext()
                .build();

        this.asyncStub = DeviceServiceGrpc.newStub(channel);
    }

    public Mono<Long> getChildIdByDeviceId(String deviceId) {
        return Mono.create(sink -> {
            DeviceRequest request = DeviceRequest.newBuilder()
                    .setDeviceId(deviceId)
                    .build();

            asyncStub.getChildByDeviceId(request, new StreamObserver<>() {
                @Override
                public void onNext(ChildResponse value) {
                    sink.success(value.getChildId());
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
