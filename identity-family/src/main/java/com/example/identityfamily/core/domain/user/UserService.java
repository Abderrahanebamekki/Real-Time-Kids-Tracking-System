package com.example.identityfamily.core.domain.user;

public interface UserService {
    public String singIn(UserRequest userRequest);
    public Long singUp(UserRequest userRequest);
    public String verificationCode(Long user_id , String code);
    public void resendCode(Long user_id);
}
