package com.example.identityfamily.core.domain.user;

public interface UserService {
    public String singIn(UserRequest userRequest);
    public String singUp(UserRequest userRequest);
}
