package com.example.identityfamily.core.domain.parentchild;


import com.example.identityfamily.core.domain.child.ChildEntity;
import com.example.identityfamily.core.domain.parent.ParentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParentChildRepository extends JpaRepository<ParentChildEntity, Long> {
    Optional<ParentChildEntity> findByParentAndChild(ParentEntity parent, ChildEntity child);
    Boolean existsByParentAndChild(ParentEntity parent, ChildEntity child);

    @Query("""
    SELECT p.userId
    FROM ParentChildEntity pc
    JOIN pc.parent p
    JOIN pc.child c
    WHERE c.id = :childId
""")
    List<Long> getAllUsersByChildId(Long childId);


}
