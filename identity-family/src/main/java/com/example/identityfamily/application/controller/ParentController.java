package com.example.identityfamily.application.controller;


import com.example.identityfamily.core.domain.parent.ParentDto;
import com.example.identityfamily.core.domain.parent.ParentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/identity/v1/parents")
@RequiredArgsConstructor
public class ParentController {

    private final ParentService parentService;

    @PostMapping("/")
    public ResponseEntity<ParentDto> addParent(@Valid @RequestBody ParentDto parentDto , HttpServletRequest request) {
        request.getHeaders("X_User_Id");

        ParentDto parent = parentService.addParent(parentDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(parent);
    }
}
