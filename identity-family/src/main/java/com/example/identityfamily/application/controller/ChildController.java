package com.example.identityfamily.application.controller;


import com.example.identityfamily.core.domain.child.ChildDto;
import com.example.identityfamily.core.domain.child.ChildService;
import com.example.identityfamily.core.domain.permission.PermissionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @DeleteMapping("/{childId}")
    public ResponseEntity<Void> deleteChild(
            @PathVariable Long childId,
            @RequestHeader("X-User-Id") String userId) {
        childService.deleteChild(childId, Long.parseLong(userId));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/")
    public ResponseEntity<List<ChildDto>> getChildrenForParent(
            @RequestHeader("X-User-Id") String userId) {
        List<ChildDto> children =  childService.getChildrenForParent(Long.parseLong(userId));
        return ResponseEntity.status(HttpStatus.CREATED).body(children);
    }

}
