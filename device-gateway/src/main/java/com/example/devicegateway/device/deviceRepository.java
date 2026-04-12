package com.example.devicegateway.device;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface deviceRepository extends R2dbcRepository<DeviceEntity, UUID> {
}
