package com.example.gateway.core.domain.verficationcode;

import com.example.gateway.core.domain.user.UserEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;


@Repository
public interface VerificationCodeRepository extends R2dbcRepository<VerificationCodeEntity, Long> {

    Mono<VerificationCodeEntity> findByCodeAndUserId(String code , Long userId);

}
