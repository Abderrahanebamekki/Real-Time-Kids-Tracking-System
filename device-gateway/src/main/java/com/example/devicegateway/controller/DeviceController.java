package com.example.devicegateway.controller;


import com.example.devicegateway.device.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/device")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping("/link_child_to_device")
    public Mono<ResponseEntity<Void>> linkChildToDevice(
            @RequestParam Long child_id,
            @RequestHeader("X-User-Id") String userId,
            @RequestParam String device_id) {

        return deviceService.linkDeviceToChild(child_id, Long.parseLong(userId), device_id)
                .map(result -> ResponseEntity
                        .ok()
                        .body(result)
                );
    }


}
