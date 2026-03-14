package com.example.identityfamily.core.domain.user;

import jakarta.persistence.PrePersist;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class UserRequest {

    private String username;
    private String password;
    private Role role;


    @PrePersist
    public void setRole(Role role) {
        this.role = Role.USER;
    }
}
