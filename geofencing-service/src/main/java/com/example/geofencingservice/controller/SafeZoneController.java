package com.example.geofencingservice.controller;

import com.example.geofencingservice.dto.SafeZoneRequest;
import com.example.geofencingservice.dto.SafeZoneResponse;
import com.example.geofencingservice.dto.SafeZoneUpdateRequest;
import com.example.geofencingservice.service.SafeZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/geofencing/safezones")
@RequiredArgsConstructor
public class SafeZoneController {
    private final SafeZoneService safeZoneService;

    @PostMapping("/")
    public Mono<ResponseEntity<Void>> createSafeZone(@RequestBody SafeZoneRequest request) {
        return safeZoneService.createSafeZone(request)
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).build());
    }

    @GetMapping("/child/{childId}")
    public Flux<SafeZoneResponse> getSafeZonesByChild(@PathVariable Long childId) {
        return safeZoneService.getAllSafeZoneByChild(childId);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteSafeZone(@PathVariable Long id) {
        return safeZoneService.deleteSafeZone(id)
                .thenReturn(ResponseEntity.noContent().build());
    }

    @PutMapping("/child/{childId}")
    public Mono<ResponseEntity<Void>> updateSafeZoneByChild(
            @PathVariable Long childId,
            @RequestBody SafeZoneUpdateRequest request) {
        return safeZoneService.updateSafeZoneByChild(childId, request)
                .thenReturn(ResponseEntity.ok().build());
    }
}
