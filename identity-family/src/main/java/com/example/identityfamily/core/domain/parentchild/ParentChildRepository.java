package com.example.identityfamily.core.domain.parentchild;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentChildRepository extends JpaRepository<ParentChildEntity, Long> {
}
