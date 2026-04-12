package com.example.devicegateway.device;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("devices")
public class DeviceEntity {

    @Id
    private UUID deviceId;
    private String secret_key;
    private boolean status;
    private int battery_level;
    private Long user_id;
    private Long child_id;

}
