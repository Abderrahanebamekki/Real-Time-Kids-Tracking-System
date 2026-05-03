package com.example.identityfamily.application.grpc;



import com.example.identityfamily.core.domain.child.ChildService;
import com.example.identityfamily.core.domain.parent.ParentService;
import com.example.family.grpc.GetParentIdRequest;
import com.example.family.grpc.GetParentIdResponse;
import com.example.family.grpc.IdentityFamilyServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class IdentityFamilyGrpcEndpoint extends IdentityFamilyServiceGrpc.IdentityFamilyServiceImplBase {

    private final ChildService childService;
    private final ParentService parentService;

    @Override
    public void getParentId(GetParentIdRequest request,
                            StreamObserver<GetParentIdResponse> responseObserver) {

        try {
            Long parentId = parentService.getParentId((Long.parseLong(request.getUserId())));
            boolean isChildValid = childService.verifyChild(Long.parseLong(request.getChild()) , parentId);
            if (!isChildValid){
                parentId= 0L;
            }
            GetParentIdResponse response = GetParentIdResponse.newBuilder()
                    .setParentId(parentId.toString())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (IllegalArgumentException e) {
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription(e.getMessage())
                            .asRuntimeException()
            );
        } catch (Exception e) {
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Unexpected server error")
                            .withCause(e)
                            .asRuntimeException()
            );
        }
    }
}
