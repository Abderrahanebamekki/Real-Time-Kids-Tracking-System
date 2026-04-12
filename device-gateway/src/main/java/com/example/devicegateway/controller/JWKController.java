package com.example.devicegateway.controller;


import com.example.devicegateway.security.JWKService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/.well-known")
@RequiredArgsConstructor
public class JWKController {

    private final JWKService jwkService;
    @GetMapping("/jwks.json")
    public Mono<Object> getJwks(){
        return Mono.just(jwkService.getPublicJwkSet().toJSONObject());
    }

}

