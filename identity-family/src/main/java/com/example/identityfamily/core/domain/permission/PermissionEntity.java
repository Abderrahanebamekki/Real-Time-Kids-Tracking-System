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

    private boolean canAddZone;
    private boolean canDeleteZone;
    private boolean canAddSafeRoute;
    private boolean canDeleteSafeRoute;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_child_id")
    private ParentChildEntity parentChild;
}
