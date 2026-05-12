package com.example.geofencingservice.safe_zone;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("safezone")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SafeZoneEntity {

    @Id
    private Long id;

    @Column("name")
    private String name;

    @Column("longitude")
    private Double longitude;

    @Column("latitude")
    private Double latitude;

    @Column("radius")
    private Double radius;

    @Column("child_id")
    private Long childId;
}
