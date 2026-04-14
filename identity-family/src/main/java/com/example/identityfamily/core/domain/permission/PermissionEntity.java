package com.example.identityfamily.core.domain.permission;

import com.example.identityfamily.core.domain.parentchild.ParentChildEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PermissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean canAddZone = false;
    private boolean canDeleteZone= false;
    private boolean canAddSafeRoute = false;
    private boolean canDeleteSafeRoute= false;

    @OneToOne(mappedBy = "permission", fetch = FetchType.LAZY)
    private ParentChildEntity parentChild;
}
