package com.rashad.TourPlanner.service;

import com.rashad.TourPlanner.Exception.TransportNotFoundException;
import com.rashad.TourPlanner.entities.Transport;
import com.rashad.TourPlanner.repo.TransportRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class TransportService {

    private static final Logger logger = Logger.getLogger(TransportService.class.getName());

    @Autowired
    private TransportRepo repo;

    public Transport addTransport(Transport transport) {
        logger.info("Adding a new transport with details: " + transport);
        Transport savedTransport = repo.save(transport);
        logger.info("Transport added successfully with ID: " + savedTransport.getId());
        return savedTransport;
    }

    public Transport getTransportById(Long id) {
        logger.info("Fetching transport with ID: " + id);
        Transport transport = repo.findById(id)
                .orElseThrow(() -> new TransportNotFoundException("Transport not found with id " + id));
        logger.info("Transport fetched successfully: " + transport);
        return transport;
    }

    public List<Transport> getAllTransports() {
        logger.info("Fetching all transports");
        List<Transport> transports = repo.findAll();
        logger.info("Fetched " + transports.size() + " transports");
        return transports;
    }

    public Transport updateTransport(Long id, Transport transportDetails) {
        logger.info("Updating transport with ID: " + id);
        Transport transport = repo.findById(id)
                .orElseThrow(() -> new TransportNotFoundException("Transport not found with id " + id));

        // Update transport details
        transport.setTransportName(transportDetails.getTransportName());
        transport.setTransportType(transportDetails.getTransportType());
        transport.setTransportDescription(transportDetails.getTransportDescription());
        transport.setEstimatedTravelTime(transportDetails.getEstimatedTravelTime());

        Transport updatedTransport = repo.save(transport);
        logger.info("Transport updated successfully: " + updatedTransport);
        return updatedTransport;
    }

    public void deleteTransport(Long id) {
        logger.info("Deleting transport with ID: " + id);
        repo.findById(id)
                .orElseThrow(() -> new TransportNotFoundException("Transport not found with id " + id));

        repo.deleteById(id);
        logger.info("Transport deleted successfully with ID: " + id);
    }
}
