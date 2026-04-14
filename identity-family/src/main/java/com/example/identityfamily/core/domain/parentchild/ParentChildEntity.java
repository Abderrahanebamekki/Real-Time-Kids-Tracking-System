package com.example.identityfamily.core.domain.parentchild;

import com.example.identityfamily.core.domain.child.ChildEntity;
import com.example.identityfamily.core.domain.parent.ParentEntity;
import com.example.identityfamily.core.domain.permission.PermissionEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Parent;


@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParentChildEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = false)
    private ParentEntity parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    private ChildEntity child;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "permission_id", unique = true)
    private PermissionEntity permission;

    private Role role;
}
