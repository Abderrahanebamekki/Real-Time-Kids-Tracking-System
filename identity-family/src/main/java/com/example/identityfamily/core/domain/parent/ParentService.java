package com.example.identityfamily.core.domain.parent;

import com.example.identityfamily.core.domain.CoParentInvitation.InvitationDto;
import com.example.identityfamily.core.domain.CoParentInvitation.InvitationStatus;

import java.util.List;

public interface ParentService {
    public ParentDto addParent(ParentDto parentDto , Long userId);
    public Long getParentId(Long userId);
    public void generateInvitation(Long userId , Long childId , String receiverPhoneNumber);
    public List<InvitationDto> getInvitations(Long userId);
    public void acceptInvitation(Long invitationId,Long userId);
    public void declineInvitation(Long invitationId,Long userId);
    List<Long> getAllParents(Long childId);
    String getFullname(Long userId);
}
