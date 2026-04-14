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

    private final ChildRepository childRepository;
    private final ParentChildRepository parentChildRepository;
    private final ParentRepository parentRepository;
    private final PermissionRepository permissionRepository;



    @Override
    public ChildDto addChild(ChildDto childDto, Long userId) {

        ParentEntity parent = parentRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Parent not found"));

        ChildEntity child = ChildMapper.mapToEntity(childDto);
        child = childRepository.save(child);

        PermissionEntity permissions = PermissionEntity.builder()
                .canAddSafeRoute(true)
                .canAddZone(true)
                .canDeleteSafeRoute(true)
                .canDeleteZone(true)
                .build();
        permissions = permissionRepository.save(permissions);

        ParentChildEntity relation = createParentChildRelation(parent, child,Role.PARENT , permissions);
        parentChildRepository.save(relation);

        return ChildMapper.mapToDto(child);
    }

    @Override
    public void addGuarantor(PermissionDto permissionDto , Long childId, Long parentId) {

        ParentEntity parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent not found"));

        ChildEntity child = childRepository.findById(childId)
                .orElseThrow(() -> new RuntimeException("Child not found"));
        PermissionEntity permission = PermissionMapper.mapToEntity(permissionDto);
        permission = permissionRepository.save(permission);

        ParentChildEntity relation = createParentChildRelation(parent, child, Role.CO_PARENT,permission);

        parentChildRepository.save(relation);

    }

    @Override
    public boolean verifyChild(Long child_id, Long parent_id) {
        ChildEntity childEntity = childRepository.findById(child_id).orElse(null);
        assert childEntity != null;
        ParentEntity parentEntity = parentRepository.findById(parent_id).orElse(null);
        assert parentEntity != null;
        if (parentChildRepository.existsByParentAndChild(parentEntity, childEntity)){
            ParentChildEntity parentChildEntity = parentChildRepository.findByParentAndChild(parentEntity, childEntity).orElse(null);
            assert parentChildEntity != null;
            return parentChildEntity.getRole().equals(Role.PARENT);
        }
        return false;
    }


    private ParentChildEntity createParentChildRelation(ParentEntity parent, ChildEntity child , Role role , PermissionEntity permission) {

        return ParentChildEntity.builder()
                .parent(parent)
                .child(child)
                .role(role)
                .permission(permission)
                .build();
    }
}
