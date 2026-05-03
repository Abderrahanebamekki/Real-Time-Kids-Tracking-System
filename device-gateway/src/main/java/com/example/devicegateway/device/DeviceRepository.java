package com.example.devicegateway.device;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface DeviceRepository extends R2dbcRepository<DeviceEntity, String> {
    Mono<DeviceEntity> findByDeviceId(String deviceId);
}
