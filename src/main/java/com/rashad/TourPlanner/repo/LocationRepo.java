package com.rashad.TourPlanner.repo;

import com.rashad.TourPlanner.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepo extends JpaRepository<Location, Long> {
    Location findTopByOrderByIdDesc();
}
