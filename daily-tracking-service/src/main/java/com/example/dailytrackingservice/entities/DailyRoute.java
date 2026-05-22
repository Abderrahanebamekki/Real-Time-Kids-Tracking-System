package com.example.dailytrackingservice.entities;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@Builder
@Table("daily_route")
public class DailyRoute {

    @Id
    private Long id;
    private String childId;
    private LocalDate date;
    private String routePoints;
}
