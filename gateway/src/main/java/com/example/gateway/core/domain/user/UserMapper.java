package com.example.gateway.core.domain.user;

public class UserMapper {

    public static UserEntity mapToEntity(UserDto userRequest) {
        return UserEntity.builder()
                .password(userRequest.getPassword())
                .username(userRequest.getUsername())
                .role(userRequest.getRole())
                .build();
    }


}
