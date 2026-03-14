package com.example.identityfamily.core.domain.child;

import com.example.identityfamily.core.domain.permission.PermissionDto;
import org.springframework.web.bind.annotation.RequestBody;

public interface ChildService {
    ChildDto addChild(ChildDto childDto , Long parent_id);
    void addGuarantor(PermissionDto permissionDto , Long child_id , Long parent_id);
}
