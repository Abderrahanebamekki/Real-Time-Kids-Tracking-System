package com.example.geofencingservice.controller;


import com.example.geofencingservice.dto.GPS;
import com.example.geofencingservice.dto.GpsSending;
import com.example.geofencingservice.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/geofencing/gps")
@RequiredArgsConstructor
public class GpsController {
   private final RedisService redisService;
   @GetMapping(value = "/{childId}" , produces = MediaType.TEXT_EVENT_STREAM_VALUE)
   public Flux<GpsSending> getGPS(@PathVariable String childId) {
       return redisService.subscribe(childId);
   }

}
