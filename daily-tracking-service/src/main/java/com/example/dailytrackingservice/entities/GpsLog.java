package com.example.dailytrackingservice.entities;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Data
@Builder
@Table("gps_log")
public class GpsLog {

    @Id
    private Long id;
    private String childId;
    private Double longitude;
    private Double latitude;
    private Double speed;
    private Instant timestamp;
}
