package com.example.identityfamily.application.controller;


import com.example.identityfamily.core.domain.user.UserRequest;
import com.example.identityfamily.core.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class UserController {

    private UserService userService;

    @PostMapping("/singin")
    public ResponseEntity<String> singIn(@RequestBody UserRequest  userRequest) {
        return ResponseEntity.ok("");
    }

    @PostMapping("/singup")
    public ResponseEntity<String> singUp(@RequestBody UserRequest  userRequest) {
        return ResponseEntity.ok("");
    }


}
