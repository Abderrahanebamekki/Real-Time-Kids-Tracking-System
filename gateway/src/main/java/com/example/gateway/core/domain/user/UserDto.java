package com.example.gateway.core.domain.user;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String username;
    private String password;
    private final Role role = Role.USER;

}
