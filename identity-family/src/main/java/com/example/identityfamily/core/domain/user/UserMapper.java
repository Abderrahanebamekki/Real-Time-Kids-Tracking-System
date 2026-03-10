package com.example.identityfamily.core.domain.user;

public class UserMapper {

    public static UserEntity mapToEntity(UserRequest userRequest) {
        return UserEntity.builder()
                .password(userRequest.getPassword())
                .username(userRequest.getUsername())
                .role(userRequest.getRole())
                .build();
    }


}
