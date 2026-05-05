package com.example.identityfamily.core.domain.parent;

import com.example.identityfamily.core.domain.CoParentInvitation.CoParentInvitationEntity;
import com.example.identityfamily.core.domain.CoParentInvitation.CoParentInvitationRepository;
import com.example.identityfamily.core.domain.CoParentInvitation.InvitationDto;
import com.example.identityfamily.core.domain.CoParentInvitation.InvitationStatus;
import com.example.identityfamily.core.domain.child.ChildEntity;
import com.example.identityfamily.core.domain.child.ChildRepository;
import com.example.identityfamily.core.domain.exception.ParentNotExist;
import com.example.identityfamily.core.domain.exception.PhoneNumberAlreadyExist;
import com.example.identityfamily.core.domain.parentchild.ParentChildEntity;
import com.example.identityfamily.core.domain.parentchild.ParentChildRepository;
import com.example.identityfamily.core.domain.parentchild.Role;
import com.example.identityfamily.core.domain.permission.PermissionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParentServiceImpl implements ParentService {

    private final ParentRepository parentRepository;
    private final ChildRepository childRepository;
    private final CoParentInvitationRepository coParentInvitationRepository;
    private final ParentChildRepository  parentChildRepository;


    @Override
    public ParentDto addParent(ParentDto parentDto , Long userId) {
        parentDto.formatPhoneNumber();
        if(parentRepository.existsByPhoneNumber(parentDto.getPhoneNumber())){
            throw new PhoneNumberAlreadyExist();
        }
        ParentEntity parentEntity = ParentMapper.mapToEntity(parentDto);
        parentEntity.setUserId(userId);
        parentEntity = parentRepository.save(parentEntity);
        return ParentMapper.mapToDto(parentEntity);
    }

    @Override
    public Long getParentId(Long userId) {
        ParentEntity parentEntity = parentRepository.findByUserId(userId).orElse(null);
        assert parentEntity != null;
        return parentEntity.getId();
    }

    @Override
    public void generateInvitation(Long userId, Long childId, String receiverPhoneNumber) {
        if (receiverPhoneNumber.startsWith("0")) {
            receiverPhoneNumber = "213" + receiverPhoneNumber.substring(1);
        }
        System.out.println(receiverPhoneNumber);
        if(!parentRepository.existsByPhoneNumber(receiverPhoneNumber)){
            throw new ParentNotExist();
        }
        ParentEntity coParent = parentRepository.findByPhoneNumber(receiverPhoneNumber);
        ParentEntity parent = parentRepository.findByUserId(userId).orElse(null);
        assert parent != null;
        ChildEntity child = childRepository.findById(childId).orElseThrow(() -> new RuntimeException("Child not found"));
        assert child != null;

        CoParentInvitationEntity invitation = CoParentInvitationEntity.builder()
                .child(child)
                .receiverParent(coParent)
                .senderParent(parent)
                .status(InvitationStatus.PENDING)
                .build();

        coParentInvitationRepository.save(invitation);

    }

    @Override
    public List<InvitationDto> getInvitations(Long userId) {
        return coParentInvitationRepository.findMyActiveInvitationsByUserId(userId, LocalDateTime.now());
    }

    @Override
    public void acceptInvitation(Long invitationId , Long userId) {
        ParentEntity coParent = parentRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Co-Parent not found"));
        CoParentInvitationEntity invitation = coParentInvitationRepository.findById(invitationId).orElseThrow(() -> new RuntimeException("Invitation not found"));
        if (invitation.getReceiverParent().getId().equals(coParent.getId())) {
            invitation.setStatus(InvitationStatus.ACCEPTED);
            coParentInvitationRepository.save(invitation);
            ParentChildEntity parentChild = ParentChildEntity.builder()
                    .role(Role.CO_PARENT)
                    .parent(coParent)
                    .child(invitation.getChild())
                    .permission(new PermissionEntity())
                    .build();
        }else {
            throw new RuntimeException("You are not the receiver of this invitation");
        }

    }

    @Override
    public void declineInvitation(Long invitationId, Long userId) {
        ParentEntity coParent = parentRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Co-Parent not found"));
        CoParentInvitationEntity invitation = coParentInvitationRepository.findById(invitationId).orElseThrow(() -> new RuntimeException("Invitation not found"));
        if (invitation.getReceiverParent().getId().equals(coParent.getId())) {
            invitation.setStatus(InvitationStatus.CANCELLED);
            coParentInvitationRepository.save(invitation);
        }else {
            throw new RuntimeException("You are not the receiver of this invitation");
        }
    }

    @Override
    public List<Long> getAllParents(Long childId) {
        return parentChildRepository.getAllUsersByChildId(childId);
    }


}
