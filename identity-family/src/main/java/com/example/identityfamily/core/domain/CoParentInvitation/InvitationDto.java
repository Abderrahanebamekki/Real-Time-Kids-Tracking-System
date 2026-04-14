package com.example.identityfamily.core.domain.CoParentInvitation;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
public class InvitationDto {
    private Long id;
    private String status;
    private String childFullName;
    private String parentFullName;

    public InvitationDto(Long id, String status, String childFullName, String parentFullName) {
        this.id = id;
        this.status = status;
        this.childFullName = childFullName;
        this.parentFullName = parentFullName;
    }

}
