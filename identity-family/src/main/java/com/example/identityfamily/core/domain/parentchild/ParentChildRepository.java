package com.example.identityfamily.core.domain.parentchild;


import com.example.identityfamily.core.domain.child.ChildEntity;
import com.example.identityfamily.core.domain.parent.ParentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParentChildRepository extends JpaRepository<ParentChildEntity, Long> {
    Optional<ParentChildEntity> findByParentAndChild(ParentEntity parent, ChildEntity child);
}
