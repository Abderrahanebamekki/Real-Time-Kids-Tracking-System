package com.example.gateway.core.domain.user;

import com.example.gateway.core.domain.exception.CodeExpiredException;
import com.example.gateway.core.domain.exception.CodeIsNotTrue;
import com.example.gateway.core.domain.exception.EmailAlreadyExists;
import com.example.gateway.core.domain.exception.UserNotFound;
import com.example.gateway.core.domain.globalservice.EmailService;
import com.example.gateway.core.domain.globalservice.JwtService;
import com.example.gateway.core.domain.verficationcode.VerificationCodeEntity;
import com.example.gateway.core.domain.verficationcode.VerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ReactiveAuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final VerificationCodeRepository verificationCodeRepository;


    @Override
    public Mono<String> signIn(UserDto request) {
        return authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()
                        )
                )
                .flatMap(auth ->
                        userRepository.findByUsername(request.getUsername())
                )
                .map(this::getToken);
    }

    @Override
    public Mono<Long> signUp(UserDto request) {
        return userRepository.findByUsername(request.getUsername())
                .flatMap(existingUser -> Mono.<Long>error(new EmailAlreadyExists(request.getUsername())))
                .switchIfEmpty(Mono.defer(() -> {
                    UserEntity newUser = UserMapper.mapToEntity(request);
                    newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
                    newUser.setCreatedAt(LocalDateTime.now());

                    return userRepository.save(newUser)
                            .flatMap(savedUser -> {
                                VerificationCodeEntity code = VerificationCodeEntity.createForUser(savedUser.getId());

                                return verificationCodeRepository.save(code)
                                        .doOnSuccess(vc -> {
                                            assert vc != null;
                                            emailService.sendVerificationEmail(
                                                    savedUser.getUsername(),
                                                    vc.getCode()
                                            );
                                        })
                                        .thenReturn(savedUser.getId());
                            });
                }));
    }

    @Override
    public Mono<String> verificationCode(Long userId, String code) {

        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new UserNotFound()))
                .flatMap(user ->
                        verificationCodeRepository.findByCodeAndUserId(code ,userId)
                                .switchIfEmpty(Mono.error(new CodeIsNotTrue()))
                                .flatMap(vc -> {

                                    if (vc.getExpiredAt().isBefore(LocalDateTime.now())) {
                                        return Mono.error(new CodeExpiredException());
                                    }

                                    return Mono.just(getToken(user));
                                })
                );
    }

    @Override
    public Mono<Void> resendCode(Long userId) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new UserNotFound()))
                .flatMap(user -> {
                    VerificationCodeEntity code = VerificationCodeEntity.createForUser(user.getId());
                    return verificationCodeRepository.save(code)
                            .flatMap(vc -> {
                                return emailService.sendVerificationEmail(user.getUsername(), vc.getCode());
                            });
                })
                .then();
    }

    private String getToken(UserEntity user) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("role", List.of("ROLE_" + user.getRole()));
        return jwtService.buildJwt(claims, user);
    }

}