package com.example.identityfamily.application.controller;


import com.example.identityfamily.core.domain.child.ChildDto;
import com.example.identityfamily.core.domain.child.ChildService;
import com.example.identityfamily.core.domain.permission.PermissionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/identity/v1/children")
@RequiredArgsConstructor
public class ChildController {

    private final ChildService childService;

    @PostMapping("/child")
    public ResponseEntity<ChildDto> addChild(
            @RequestBody ChildDto childDto,
            @RequestHeader("X-User-Id") String userId) {

        ChildDto child = childService.addChild(childDto, Long.parseLong(userId));

        return ResponseEntity.status(HttpStatus.CREATED).body(child);
    }

    @PostMapping("/")
    public ResponseEntity<ChildDto> getAllChildForParent(
            @RequestHeader("X-User-Id") String userId) {

        return ResponseEntity.status(HttpStatus.CREATED).body(child);
    }

}
