package com.infrasub.backend.controller;

import com.infrasub.backend.dto.InSARMeasurementResponse;
import com.infrasub.backend.service.InSARService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/insar")
public class InSARController {

    private final InSARService service;

    public InSARController(InSARService service) {
        this.service = service;
    }

    @GetMapping("/measurements")
    public Page<InSARMeasurementResponse> getMeasurements(Pageable pageable) {
        return service.getAllMeasurements(pageable);
    }

    @GetMapping("/nearby")
    public List<InSARMeasurementResponse> getNearbyMeasurements(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "1000") double radius) {

        return service.getNearbyMeasurements(latitude, longitude, radius);
    }
}