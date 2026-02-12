package com.rashad.TourPlanner.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tour")
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String tourName;
    private String tourDescription;
    private String tourGuide;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;


    @ElementCollection
    @CollectionTable(name = "tour_meal", joinColumns = @JoinColumn(name = "tour_id"))
    @Column(name = "meal")
    private List<String> meals = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "taou_activities", joinColumns = @JoinColumn(name = "tour_id"))
    @Column(name = "activity")
    private List<String> activities = new ArrayList<>();

    private Double price;
    private Integer ticketsAvailable;

    @ElementCollection
    @CollectionTable(name = "tour_images", joinColumns = @JoinColumn(name = "tour_id"))
    @Column(name = "image")
    private List<String> tourImages = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    @OneToOne
    @JoinColumn(name = "lodging_id", referencedColumnName = "id")
    private Lodging lodging;

    @OneToOne
    @JoinColumn(name = "transport_id", referencedColumnName = "id")
    private Transport transport;
}
