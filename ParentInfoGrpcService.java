package com.example.family.service;

import com.example.family.proto.GetParentInfoRequest;
import com.example.family.proto.GetParentInfoResponse;
import com.example.family.proto.ParentInfoServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@GrpcService
public class ParentInfoGrpcService extends ParentInfoServiceGrpc.ParentInfoServiceImplBase {

    private static final Pattern CHILD_ID_PATTERN = Pattern.compile("^CHILD_\\d+$");

    @Override
    public void getParentInfo(GetParentInfoRequest request, StreamObserver<GetParentInfoResponse> responseObserver) {
        String childId = request.getChildId();

        if (childId == null || childId.isEmpty() || !CHILD_ID_PATTERN.matcher(childId).matches()) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid childId: must match pattern ^CHILD_\\d+$")
                    .asRuntimeException());
            return;
        }

        GetParentInfoResponse response = GetParentInfoResponse.newBuilder()
                .setParentId("PARENT_001")
                .setIsValid(true)
                .setErrorMessage("")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
