package com.infrasub.backend.service;

import com.infrasub.backend.dto.InSARMeasurementResponse;
import com.infrasub.backend.entity.InSARMeasurement;
import com.infrasub.backend.repository.InSARMeasurementRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class InSARService {

    private final InSARMeasurementRepository repository;

    public InSARService(InSARMeasurementRepository repository) {
        this.repository = repository;
    }

    public Page<InSARMeasurementResponse> getAllMeasurements(Pageable pageable) {
        return repository.findAll(pageable)
                .map(this::mapToResponse);
    }

    public List<InSARMeasurementResponse> getNearbyMeasurements(
            double latitude,
            double longitude,
            double radius) {

        List<InSARMeasurement> measurements = repository.findNearbyMeasurements(latitude, longitude, radius);
        return measurements.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private InSARMeasurementResponse mapToResponse(InSARMeasurement measurement) {
        return new InSARMeasurementResponse(
                measurement.getId(),
                measurement.getLatitude(),
                measurement.getLongitude(),
                measurement.getVlmCmPerYear(),
                measurement.getVlmMmPerYear()
        );
    }
}