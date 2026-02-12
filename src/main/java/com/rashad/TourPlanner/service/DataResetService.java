package com.rashad.TourPlanner.service;

import com.rashad.TourPlanner.repo.LocationRepo;
import com.rashad.TourPlanner.repo.LodgingRepo;
import com.rashad.TourPlanner.repo.TourRepo;
import com.rashad.TourPlanner.repo.TransportRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class DataResetService {

    private final TourRepo tourRepository;
    private final LocationRepo locationRepository;
    private final LodgingRepo lodgingRepository;
    private final TransportRepo transportRepository;

    public DataResetService(TourRepo tourRepository, LocationRepo locationRepository, LodgingRepo lodgingRepository, TransportRepo transportRepository) {
        this.tourRepository = tourRepository;
        this.lodgingRepository = lodgingRepository;
        this.locationRepository = locationRepository;
        this.transportRepository = transportRepository;
    }

    @Transactional
    public void resetLocationData() {
        tourRepository.deleteAll();      // 🔥 child table
        locationRepository.deleteAll();  // 🔥 parent table
        lodgingRepository.deleteAll();
        transportRepository.deleteAll();
    }
}
