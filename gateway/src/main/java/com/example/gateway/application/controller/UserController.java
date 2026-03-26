package com.example.gateway.application.controller;

import com.example.gateway.core.domain.user.UserDto;
import com.example.gateway.core.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signin")
    public Mono<ResponseEntity<String>> signIn(@RequestBody UserDto userRequest) {
        return userService.signIn(userRequest)
                .map(token -> ResponseEntity.ok().body(token));
    }

    @PostMapping("/signup")
    public Mono<ResponseEntity<String>> signUp(@RequestBody UserDto userRequest) {
        return userService.signUp(userRequest)
                .map(userId -> ResponseEntity.status(201).body(userId.toString()));
    }

    @PostMapping("/verify/{userId}/{code}")
    public Mono<ResponseEntity<String>> verifyCode(
            @PathVariable Long userId,
            @PathVariable String code) {

        return userService.verificationCode(userId, code)
                .map(token -> ResponseEntity.ok().body(token));
    }

    @PostMapping("/resend/{userId}")
    public Mono<ResponseEntity<String>> resendCode(@PathVariable Long userId) {
        return userService.resendCode(userId)
                .thenReturn(ResponseEntity.ok().body("Verification code sent successfully."));
    }
}