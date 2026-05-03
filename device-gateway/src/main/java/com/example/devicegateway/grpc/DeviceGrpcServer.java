package com.example.devicegateway.grpc;

import com.example.device.grpc.ChildResponse;
import com.example.device.grpc.DeviceRequest;
import com.example.device.grpc.DeviceServiceGrpc;
import com.example.devicegateway.device.DeviceRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class DeviceGrpcServer extends DeviceServiceGrpc.DeviceServiceImplBase{

    private final DeviceRepository deviceRepository;

    @Override
    public void getChildByDeviceId(DeviceRequest request, StreamObserver<ChildResponse> responseObserver) {
        deviceRepository.findByDeviceId(request.getDeviceId())
                .map(deviceEntity -> ChildResponse.newBuilder()
                        .setChildId(deviceEntity.getChildId())
                        .build())
                .subscribe(responseObserver::onNext, responseObserver::onError);
    }


}
