package com.example.identityfamily.core.domain.parent;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParentRepository extends JpaRepository<ParentEntity, Long> {
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<ParentEntity> findByUserId(long userId);
}
