package com.example.identityfamily.application.controller;


import com.example.identityfamily.core.domain.CoParentInvitation.InvitationDto;
import com.example.identityfamily.core.domain.parent.ParentDto;
import com.example.identityfamily.core.domain.parent.ParentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/identity/v1/parents")
@RequiredArgsConstructor
public class ParentController {

    private final ParentService parentService;

    @PostMapping("/")
    public ResponseEntity<ParentDto> addParent(@Valid @RequestBody ParentDto parentDto , @RequestHeader("X-User-Id") String userId) {
        ParentDto parent = parentService.addParent(parentDto,Long.parseLong(userId));

        return ResponseEntity.status(HttpStatus.CREATED).body(parent);
    }

    @PostMapping("/invitation/{childId}/{receiverPhoneNumber}")
    public ResponseEntity<?> generateInvitation(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable Long childId,
            @PathVariable String receiverPhoneNumber
    ){
        parentService.generateInvitation(Long.parseLong(userId) , childId , receiverPhoneNumber);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/invitations")
    public ResponseEntity<List<InvitationDto>> getInvitations(
            @RequestHeader("X-User-Id") String userId) {

        List<InvitationDto> invitations = parentService.getInvitations(Long.parseLong(userId));
        return ResponseEntity.ok(invitations);
    }

    @PatchMapping("/invitations/{invitationId}/accept")
    public ResponseEntity<Void> acceptInvitation(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable Long invitationId) {

        parentService.acceptInvitation(invitationId, Long.parseLong(userId));
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/invitations/{invitationId}/decline")
    public ResponseEntity<Void> declineInvitation(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable Long invitationId) {

        parentService.declineInvitation(invitationId, Long.parseLong(userId));
        return ResponseEntity.ok().build();
    }

}
