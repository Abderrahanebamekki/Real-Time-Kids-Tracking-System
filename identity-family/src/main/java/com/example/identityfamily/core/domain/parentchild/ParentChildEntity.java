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
    private ParentEntity parent;

    @ManyToOne(fetch = FetchType.LAZY)
    private ChildEntity child;

    @OneToOne(mappedBy = "parentChild")
    private PermissionEntity permission;

    private Role role;
}
