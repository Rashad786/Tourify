package com.rashad.TourPlanner.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "lodging")
public class Lodging {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String lodgingName;
    private String lodgingType;
    private String lodgingDescription;
    private String address;
    private Double rating;
}
