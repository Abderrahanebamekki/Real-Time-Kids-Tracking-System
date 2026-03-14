package com.example.identityfamily.core.domain.verficationcode;


import com.example.identityfamily.core.domain.globalservice.VerificationCodeGenerator;
import com.example.identityfamily.core.domain.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerificationCodeEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String code;

    private LocalDateTime expiredAt;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @PrePersist
    public void onCreate() {
        this.code = VerificationCodeGenerator.generateCode();
        this.expiredAt = LocalDateTime.now().plusMinutes(1).plusSeconds(30);
    }



}
