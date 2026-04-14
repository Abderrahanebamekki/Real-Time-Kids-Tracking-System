package com.example.identityfamily.core.domain.parent;



public class ParentMapper {

    public static ParentEntity mapToEntity(ParentDto parentDto) {
        return ParentEntity.builder()
                .firstName(parentDto.getFirstName())
                .lastName(parentDto.getLastName())
                .phoneNumber(parentDto.getPhoneNumber())
                .build();
    }

    public static ParentDto mapToDto(ParentEntity parentEntity) {
        return ParentDto.builder()
                .id(parentEntity.getId())
                .firstName(parentEntity.getFirstName())
                .lastName(parentEntity.getLastName())
                .phoneNumber(parentEntity.getPhoneNumber())
                .build();
    }

}
