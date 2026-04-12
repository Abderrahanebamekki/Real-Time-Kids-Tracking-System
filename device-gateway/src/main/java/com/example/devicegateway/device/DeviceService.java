package com.example.devicegateway.device;

import reactor.core.publisher.Mono;

public interface DeviceService {
    public Mono<Void> linkDeviceToChild(Long childId, Long parentId , String deviceId); // user
    public Mono<Void> registerDevice(); // admin

}
