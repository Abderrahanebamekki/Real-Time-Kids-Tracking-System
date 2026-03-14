package com.example.identityfamily.core.domain.child;


import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChildDto {

    private Long id;

    private String firstName;

    private String lastName;

}
