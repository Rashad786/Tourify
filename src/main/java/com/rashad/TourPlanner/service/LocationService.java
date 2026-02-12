package com.rashad.TourPlanner.service;

import com.rashad.TourPlanner.Exception.LocationNotFoundException;
import com.rashad.TourPlanner.entities.Location;
import com.rashad.TourPlanner.repo.LocationRepo;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class LocationService {

    private static final Logger logger = Logger.getLogger(LocationService.class.getName());

    @Autowired
    private LocationRepo repo;

    // Add a new location
    public Location addLocation(Location location) {
        logger.info("Adding a new location with details: " + location);
        Location savedLocation = repo.save(location);
        logger.info("Location added successfully with ID: " + savedLocation.getId());
        return savedLocation;
    }

    // Get location by ID
    public Optional<Location> getLocationById(Long id) {
        logger.info("Fetching location with ID: " + id);
        Optional<Location> location = Optional.ofNullable(repo.findById(id)
                .orElseThrow(() -> new LocationNotFoundException("Location not found with id " + id)));
        logger.info("Location fetched successfully: " + location);
        return location;
    }

    // Get all locations
    public List<Location> getAllLocations() {
        logger.info("Fetching all locations");
        List<Location> locations = repo.findAll();
        logger.info("Fetched " + locations.size() + " locations");
        return locations;
    }

    // Update location
    @Transactional
    public Location updateLocation(Long id, Location locationDetails) {
        logger.info("Updating location with ID: " + id);
        Location location = repo.findById(id)
                .orElseThrow(() -> new LocationNotFoundException("Location not found with id " + id));

        // Update location details
        location.setFromLocation(locationDetails.getFromLocation());
        location.setToLocation(locationDetails.getToLocation());
        location.setCountry(locationDetails.getCountry());
        location.setDistance(locationDetails.getDistance());
        location.setLocationDescription(locationDetails.getLocationDescription());
        location.setEstimatedTravelTime(locationDetails.getEstimatedTravelTime());

        Location updatedLocation = repo.save(location);
        logger.info("Location updated successfully: " + updatedLocation);
        return updatedLocation;
    }

    // Delete location
    public void deleteLocation(Long id) {
        logger.info("Deleting location with ID: " + id);
        repo.findById(id)
                .orElseThrow(() -> new LocationNotFoundException("Location not found with id " + id));

        repo.deleteById(id);
        logger.info("Location deleted successfully with ID: " + id);
    }
}
