package com.example.devicegateway.controller;


import com.example.devicegateway.device.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/device")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping("/link_child_to_device/{parent_id}")
    public Mono<ResponseEntity<Void>> linkChildToDevice(
            @RequestParam Long child_id,
            @PathVariable Long parent_id,
            @RequestParam String device_id) {

        return deviceService.linkDeviceToChild(child_id, parent_id, device_id)
                .map(result -> ResponseEntity
                        .ok()
                        .body(result)
                );
    }

}
