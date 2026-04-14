package com.example.devicegateway.device;

import reactor.core.publisher.Mono;

public interface DeviceService {
    public Mono<Void> linkDeviceToChild(Long childId, Long userId , String deviceId); // user
}
