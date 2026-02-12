package com.rashad.TourPlanner.service;

import com.rashad.TourPlanner.Exception.LodgingNotFoundException;
import com.rashad.TourPlanner.entities.Lodging;
import java.util.logging.Logger;

import com.rashad.TourPlanner.repo.LodgingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LodgingService {

    private static final Logger logger = Logger.getLogger(LodgingService.class.getName());

    @Autowired
    private LodgingRepo repo;

    public Lodging addLodging(Lodging lodging) {
        logger.info("Adding a new lodging with details: " + lodging);
        Lodging savedLodging = repo.save(lodging);
        logger.info("Lodging added successfully with ID: " + savedLodging.getId());
        return savedLodging;
    }

    public Lodging getLodgingById(Long id) {
        logger.info("Fetching lodging with ID: " + id);
        Lodging lodging = repo.findById(id)
                .orElseThrow(() -> new LodgingNotFoundException("Lodging not found with id " + id));
        logger.info("Lodging fetched successfully: " + lodging);
        return lodging;
    }

    public List<Lodging> getAllLodgings() {
        logger.info("Fetching all lodgings");
        List<Lodging> lodgings = repo.findAll();
        logger.info("Fetched " + lodgings.size() + " lodgings");
        return lodgings;
    }

    public Lodging updateLodging(Long id, Lodging lodgingDetails) {
        logger.info("Updating lodging with ID: " + id);
        Lodging lodging = repo.findById(id)
                .orElseThrow(() -> new LodgingNotFoundException("Lodging not found with id " + id));

        // Update Lodging details
        lodging.setLodgingName(lodgingDetails.getLodgingName());
        lodging.setLodgingType(lodgingDetails.getLodgingType());
        lodging.setLodgingDescription(lodgingDetails.getLodgingDescription());
        lodging.setAddress(lodgingDetails.getAddress());
        lodging.setRating(lodgingDetails.getRating());

        Lodging updatedLodging = repo.save(lodging);
        logger.info("Lodging updated successfully: " + updatedLodging);
        return updatedLodging;
    }

    public void deleteLodging(Long id) {
        logger.info("Deleting lodging with ID: " + id);
        repo.findById(id)
                .orElseThrow(() -> new LodgingNotFoundException("Lodging not found with id " + id));

        repo.deleteById(id);
        logger.info("Lodging deleted successfully with ID: " + id);
    }
}
