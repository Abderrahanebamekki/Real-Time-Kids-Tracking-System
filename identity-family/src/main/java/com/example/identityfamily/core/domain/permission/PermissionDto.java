package com.example.identityfamily.core.domain.permission;


import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDto {
    private Long id;

    private boolean canAddZone;
    private boolean canDeleteZone;
    private boolean canAddSafeRoute;
    private boolean canDeleteSafeRoute;
}
