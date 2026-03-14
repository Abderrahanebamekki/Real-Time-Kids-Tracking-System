package com.example.identityfamily.application.controller;


import com.example.identityfamily.core.domain.child.ChildDto;
import com.example.identityfamily.core.domain.child.ChildService;
import com.example.identityfamily.core.domain.permission.PermissionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/children")
@RequiredArgsConstructor
public class ChildController {

    private final ChildService childService;

    @PostMapping("/parent/{parentId}")
    public ResponseEntity<ChildDto> addChild(
            @RequestBody ChildDto childDto,
            @PathVariable Long parentId) {

        ChildDto child = childService.addChild(childDto, parentId);

        return ResponseEntity.status(HttpStatus.CREATED).body(child);
    }

    @PostMapping("/{childId}/guarantors/{parentId}")
    public ResponseEntity<Void> addGuarantor(
            @RequestBody PermissionDto permissionDto,
            @PathVariable Long parentId,
            @PathVariable Long childId) {

        childService.addGuarantor(permissionDto, parentId, childId);

        return ResponseEntity.noContent().build();
    }
}
