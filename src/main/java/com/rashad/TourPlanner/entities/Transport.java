package com.rashad.TourPlanner.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transport")
public class Transport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String transportName;
    private String transportType;
    private String estimatedTravelTime;
    private String transportDescription;
}
