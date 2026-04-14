package com.example.identityfamily.core.domain.CoParentInvitation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.identityfamily.core.domain.CoParentInvitation.InvitationDto;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CoParentInvitationRepository extends JpaRepository<CoParentInvitationEntity, Long> {

    @Query("""
    SELECT new com.example.identityfamily.core.domain.CoParentInvitation.InvitationDto(
        i.id,
        cast(i.status as string ) ,
        CONCAT(c.firstName, ' ', c.lastName),
        CONCAT(p.firstName, ' ', p.lastName)
    )
    FROM CoParentInvitationEntity i
    JOIN i.child c
    JOIN i.senderParent p
    WHERE i.receiverParent.userId = :userId
      AND i.expiresAt > :now
      AND i.status = com.example.identityfamily.core.domain.CoParentInvitation.InvitationStatus.PENDING
""")
    List<InvitationDto> findMyActiveInvitationsByUserId(
            @Param("userId") Long userId,
            @Param("now") LocalDateTime now
    );
}
