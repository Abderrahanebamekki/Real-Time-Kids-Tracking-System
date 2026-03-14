package com.example.identityfamily.core.domain.parent;


import com.example.identityfamily.core.domain.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParentEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @OneToOne(fetch = FetchType.LAZY)
    private UserEntity user;

}
