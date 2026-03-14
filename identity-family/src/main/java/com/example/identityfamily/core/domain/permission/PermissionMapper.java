package com.example.identityfamily.core.domain.permission;

public class PermissionMapper {
    public static PermissionEntity mapToEntity(PermissionDto permissionDto) {
        return PermissionEntity.builder()
                .canAddSafeRoute(permissionDto.isCanAddSafeRoute())
                .canDeleteZone(permissionDto.isCanDeleteZone())
                .canAddZone(permissionDto.isCanAddZone())
                .canDeleteSafeRoute(permissionDto.isCanDeleteSafeRoute())
                .build();
    }
}
