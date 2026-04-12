package com.example.identityfamily.grpc;

import com.example.identityfamily.core.domain.child.ChildEntity;
import com.example.identityfamily.core.domain.child.ChildRepository;
import com.example.identityfamily.core.domain.parent.ParentEntity;
import com.example.identityfamily.core.domain.parent.ParentRepository;
import com.example.identityfamily.core.domain.parentchild.ParentChildEntity;
import com.example.identityfamily.core.domain.parentchild.ParentChildRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class FamilyServiceImpl extends FamilyServiceGrpc.FamilyServiceImplBase {

    private final ParentChildRepository parentChildRepository;
    private final ChildRepository childRepository;
    private final ParentRepository parentRepository;

    @Override
    public void validateChild(ValidateChildRequest request, StreamObserver<ValidateChildResponse> responseObserver) {
        String userId = request.getUserId();
        String childId = request.getChildId();

        log.info("Received validation request for userId: {} and childId: {}", userId, childId);

        ValidateChildResponse response;

        try {
            // Parse childId as Long to find the child entity
            long childIdLong;
            try {
                childIdLong = Long.parseLong(childId);
            } catch (NumberFormatException e) {
                response = ValidateChildResponse.newBuilder()
                        .setValid(false)
                        .setParentId("")
                        .setMessage("Invalid childId format")
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                return;
            }

            // Check if child exists
            Optional<ChildEntity> childOpt = 
                childRepository.findById(childIdLong);

            if (childOpt.isEmpty()) {
                response = ValidateChildResponse.newBuilder()
                        .setValid(false)
                        .setParentId("")
                        .setMessage("Child not found")
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                return;
            }

            // Parse userId as Long to find the parent
            long userIdLong;
            try {
                userIdLong = Long.parseLong(userId);
            } catch (NumberFormatException e) {
                response = ValidateChildResponse.newBuilder()
                        .setValid(false)
                        .setParentId("")
                        .setMessage("Invalid userId format")
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                return;
            }

            // Find parent by user_id
            Optional<ParentEntity> parentOpt = parentRepository.findByUserId(userIdLong);

            if (parentOpt.isEmpty()) {
                response = ValidateChildResponse.newBuilder()
                        .setValid(false)
                        .setParentId("")
                        .setMessage("Parent not found for given userId")
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                return;
            }

            ParentEntity parent = parentOpt.get();

            // Check if this parent has a relationship with this child
            Optional<ParentChildEntity> parentChildOpt = 
                parentChildRepository.findByParentAndChild(parent, childOpt.get());

            if (parentChildOpt.isPresent()) {
                // Valid relationship found
                response = ValidateChildResponse.newBuilder()
                        .setValid(true)
                        .setParentId(String.valueOf(parent.getId()))
                        .setMessage("Child belongs to the user")
                        .build();
            } else {
                // No relationship found
                response = ValidateChildResponse.newBuilder()
                        .setValid(false)
                        .setParentId(String.valueOf(parent.getId()))
                        .setMessage("Child does not belong to this user")
                        .build();
            }

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            log.error("Error validating child relationship", e);
            responseObserver.onError(
                Status.INTERNAL
                    .withDescription("Internal server error: " + e.getMessage())
                    .asRuntimeException()
            );
        }
    }
}
