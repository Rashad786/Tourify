package com.rashad.TourPlanner.controller;

import com.rashad.TourPlanner.service.DataResetService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/reset")
@PreAuthorize("hasRole('ADMIN')")
public class AdminResetController {

    private final DataResetService dataResetService;

    public AdminResetController(DataResetService dataResetService) {
        this.dataResetService = dataResetService;
    }

    @DeleteMapping("/location")
    public ResponseEntity<String> resetLocation() {
        dataResetService.resetLocationData();
        return ResponseEntity.ok("Tours and locations deleted successfully");
    }
}

