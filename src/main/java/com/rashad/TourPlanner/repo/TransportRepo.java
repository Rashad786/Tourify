package com.rashad.TourPlanner.repo;

import com.rashad.TourPlanner.entities.Location;
import com.rashad.TourPlanner.entities.Transport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportRepo  extends JpaRepository<Transport, Long> {
    Transport findTopByOrderByIdDesc();
}
