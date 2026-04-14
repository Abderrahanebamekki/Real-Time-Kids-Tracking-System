package com.example.identityfamily.core.domain.child;

public class ChildMapper {


    public static ChildEntity mapToEntity(ChildDto childDto) {
        return ChildEntity.builder()
                .firstName(childDto.getFirstName())
                .lastName(childDto.getLastName())
                .age(childDto.getAge())
                .build();

    }

    public static ChildDto mapToDto(ChildEntity childEntity) {
        return ChildDto.builder()
                .id(childEntity.getId())
                .firstName(childEntity.getFirstName())
                .lastName(childEntity.getLastName())
                .age(childEntity.getAge())
                .build();
    }


}
