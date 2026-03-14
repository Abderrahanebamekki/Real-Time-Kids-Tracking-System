package com.example.identityfamily.core.domain.verficationcode;

import com.example.identityfamily.core.domain.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCodeEntity, Long> {

    Optional<VerificationCodeEntity> findByCodeAndUser(String code, UserEntity user);

}
