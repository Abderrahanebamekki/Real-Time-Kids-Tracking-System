package com.example.dailytrackingservice.entities;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Data
@Builder
@Table("vitals")
public class Vitals {

    @Id
    private Long id;
    private String childId;
    private Integer heartbeats;
    private Integer oxygenLevel;
    private Instant timestamp;
}
