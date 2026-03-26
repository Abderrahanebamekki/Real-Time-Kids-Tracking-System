package com.example.gateway.core.domain.verficationcode;

import com.example.gateway.core.domain.globalservice.VerificationCodeGenerator;
import com.example.gateway.core.domain.user.UserEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.MappedCollection;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("verification_code")
public class VerificationCodeEntity {

    @Id
    private Long id;

    private String code;

    @Column("expired_at")
    private LocalDateTime expiredAt;

    @Column("user_id")
    private Long userId;

    public static VerificationCodeEntity createForUser(Long userId) {
        return VerificationCodeEntity.builder()
                .userId(userId)
                .code(VerificationCodeGenerator.generateCode())
                .expiredAt(LocalDateTime.now().plusMinutes(1).plusSeconds(30))
                .build();
    }
}