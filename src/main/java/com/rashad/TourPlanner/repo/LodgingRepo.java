package com.rashad.TourPlanner.repo;

import com.rashad.TourPlanner.entities.Location;
import com.rashad.TourPlanner.entities.Lodging;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LodgingRepo extends JpaRepository<Lodging, Long> {
    Lodging findTopByOrderByIdDesc();
}
