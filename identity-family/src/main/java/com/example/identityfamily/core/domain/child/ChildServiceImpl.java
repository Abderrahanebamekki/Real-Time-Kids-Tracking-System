package com.example.identityfamily.core.domain.child;


import com.example.identityfamily.core.domain.parent.ParentEntity;
import com.example.identityfamily.core.domain.parent.ParentRepository;
import com.example.identityfamily.core.domain.parentchild.ParentChildEntity;
import com.example.identityfamily.core.domain.parentchild.ParentChildRepository;
import com.example.identityfamily.core.domain.parentchild.Role;
import com.example.identityfamily.core.domain.permission.PermissionDto;
import com.example.identityfamily.core.domain.permission.PermissionEntity;
import com.example.identityfamily.core.domain.permission.PermissionMapper;
import com.example.identityfamily.core.domain.permission.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChildServiceImpl implements ChildService {

    private ChildRepository childRepository;
    private ParentChildRepository parentChildRepository;
    private ParentRepository parentRepository;
    private PermissionRepository permissionRepository;



    @Override
    public ChildDto addChild(ChildDto childDto, Long parentId) {

        ParentEntity parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent not found"));

        ChildEntity child = ChildMapper.mapToEntity(childDto);
        child = childRepository.save(child);

        ParentChildEntity relation = createParentChildRelation(parent, child,Role.PARENT);
        parentChildRepository.save(relation);

        return ChildMapper.mapToDto(child);
    }

    @Override
    public void addGuarantor(PermissionDto permissionDto , Long childId, Long parentId) {

        ParentEntity parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent not found"));

        ChildEntity child = childRepository.findById(childId)
                .orElseThrow(() -> new RuntimeException("Child not found"));

        ParentChildEntity relation = createParentChildRelation(parent, child, Role.CO_PARENT);

        relation = parentChildRepository.save(relation);
        PermissionEntity permission = PermissionMapper.mapToEntity(permissionDto);
        permission.setParentChild(relation);
        permissionRepository.save(permission);
    }


    private ParentChildEntity createParentChildRelation(ParentEntity parent, ChildEntity child , Role role) {

        return ParentChildEntity.builder()
                .parent(parent)
                .child(child)
                .role(role)
                .build();
    }
}
