package com.example.devicegateway.grpc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidateChildResponse {
    private boolean valid;
    private String parentId;
    private String message;
}
