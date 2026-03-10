package com.example.identityfamily.core.domain.user;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class UserRequest {

    private String username;
    private String password;
    private Role role;

    public UserRequest(){
        this.role = Role.USER;
    }

}
