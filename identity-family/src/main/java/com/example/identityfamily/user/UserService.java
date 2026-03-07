package com.example.identityfamily.user;

public interface UserService {
    public String singIn(UserRequest userRequest);
    public String singUp(UserRequest userRequest);
}
