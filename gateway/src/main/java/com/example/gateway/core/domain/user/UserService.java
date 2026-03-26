package com.example.gateway.core.domain.user;

import reactor.core.publisher.Mono;

public interface UserService {

    // ================= SIGN IN =================
    public Mono<String> signIn(UserDto request);

    // ================= SIGN UP =================
    public Mono<Long> signUp(UserDto request);

    public Mono<String> verificationCode(Long user_id , String code);
    public Mono<Void>  resendCode(Long user_id);
}
