package com.example.devicegateway.device;

import com.example.devicegateway.execption.DataNotValid;
import com.example.devicegateway.grpc.IdentityFamilyGrpcClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {
    private final IdentityFamilyGrpcClient grpcClient;
    private final DeviceRepository deviceRepository;

    @Override
    public Mono<Void> linkDeviceToChild(Long childId, Long userId, String deviceId) {
        return grpcClient.getParentId(userId.toString(), childId.toString())
                .flatMap(parentIdValue -> {
                    if (parentIdValue == null || parentIdValue.isBlank() || "0".equals(parentIdValue)) {
                        return Mono.error(new DataNotValid());
                    }


                    return deviceRepository.findById(deviceId)
                            .switchIfEmpty(Mono.error(new DataNotValid()))
                            .flatMap(deviceEntity -> {
                                deviceEntity.setChildId(childId);
                                deviceEntity.setParentId(Long.parseLong(parentIdValue));
                                return deviceRepository.save(deviceEntity);
                            })
                            .then();
                });
    }

}
