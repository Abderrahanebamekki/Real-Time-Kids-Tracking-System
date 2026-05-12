package com.example.identityfamily.core.domain.child;

import com.example.identityfamily.core.domain.permission.PermissionDto;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ChildService {
    ChildDto addChild(ChildDto childDto , Long userId);
    void addGuarantor(PermissionDto permissionDto , Long child_id , Long parent_id);
    boolean verifyChild(Long child_id , Long parent_id);
    String getChildName(Long child_id);
    List<ChildDto> getAllChildForParent(Long userId);
}
