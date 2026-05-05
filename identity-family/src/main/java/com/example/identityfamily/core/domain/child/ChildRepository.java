package com.example.identityfamily.core.domain.child;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChildRepository extends JpaRepository<ChildEntity, Long> {

    @Query("SELECT c.firstName || ' ' || c.lastName FROM ChildEntity c WHERE c.id = :child_id")
    String getChildName(Long child_id);

}
