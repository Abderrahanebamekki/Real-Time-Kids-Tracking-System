package com.example.identityfamily.application.controller;


import com.example.identityfamily.core.domain.user.UserRequest;
import com.example.identityfamily.core.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<String> signIn(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.singIn(userRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody UserRequest userRequest) {
        Long id = userService.singUp(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(id.toString());
    }

    @PostMapping("/verify/{user_id}/{code}")
    public ResponseEntity<String> verifyCode(
            @PathVariable Long user_id,
            @PathVariable String code) {

        String token = userService.verificationCode(user_id, code);
        return ResponseEntity.ok(token); // 200 OK
    }

    @PostMapping("/resend/{user_id}")
    public ResponseEntity<String> resendCode(@PathVariable Long user_id) {

        userService.resendCode(user_id);

        return ResponseEntity.ok("Verification code sent successfully.");
    }




}
