package com.example.identityfamily.core.domain.child;


import com.example.identityfamily.core.domain.parentchild.ParentChildEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChildEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private int age ;

    @OneToMany(mappedBy = "child")
    private List<ParentChildEntity> parents;
}
