package com.example.devicegateway.device;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DeviceServiceImpl implements DeviceService {
    @Override
    public Mono<Void> linkDeviceToChild(Long childId, Long userId, String deviceId) {

    }

    @Override
    public Mono<Void> registerDevice() {

    }
}
