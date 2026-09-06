package com.infrasub.backend.service;

import com.infrasub.backend.dto.VlmAnalysisResponse;
import com.infrasub.backend.entity.InSARMeasurement;
import com.infrasub.backend.repository.InSARMeasurementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.DoubleSummaryStatistics;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class VlmAnalysisService {

    private final InSARMeasurementRepository repository;

    public VlmAnalysisService(InSARMeasurementRepository repository) {
        this.repository = repository;
    }

    public VlmAnalysisResponse analyzeArea(double latitude, double longitude, double radiusMeters) {
        List<InSARMeasurement> measurements = repository.findNearbyMeasurements(latitude, longitude, radiusMeters);

        if (measurements.isEmpty()) {
            return new VlmAnalysisResponse(
                    latitude, longitude, radiusMeters,
                    0, 0.0, 0.0, 0.0, 0.0, "NO_DATA"
            );
        }

        DoubleSummaryStatistics stats = measurements.stream()
                .mapToDouble(InSARMeasurement::getVlmMmPerYear)
                .summaryStatistics();

        long negativeCount = measurements.stream()
                .filter(m -> m.getVlmMmPerYear() < 0)
                .count();

        double negativePct = round((double) negativeCount / measurements.size() * 100.0, 1);
        double avgVlm = round(stats.getAverage(), 2);
        double minVlm = round(stats.getMin(), 2);
        double maxVlm = round(stats.getMax(), 2);

        String deformationStatus;
        if (avgVlm < -3.0 || (negativePct > 75.0 && minVlm < -5.0)) {
            deformationStatus = "SEVERE_SUBSIDENCE";
        } else if (avgVlm < -1.0 || negativePct > 50.0) {
            deformationStatus = "MODERATE_SUBSIDENCE";
        } else if (avgVlm > 2.0) {
            deformationStatus = "UPLIFT";
        } else {
            deformationStatus = "STABLE";
        }

        return new VlmAnalysisResponse(
                latitude,
                longitude,
                radiusMeters,
                measurements.size(),
                avgVlm,
                minVlm,
                maxVlm,
                negativePct,
                deformationStatus
        );
    }

    private double round(double value, int places) {
        if (Double.isNaN(value) || Double.isInfinite(value)) return 0.0;
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
