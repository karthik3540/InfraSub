package com.infrasub.backend.service;

import com.infrasub.backend.dto.RiskAssessmentResponse;
import com.infrasub.backend.dto.RiskFactor;
import com.infrasub.backend.dto.VlmAnalysisResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RiskAssessmentService {

    private final VlmAnalysisService vlmAnalysisService;

    public RiskAssessmentService(VlmAnalysisService vlmAnalysisService) {
        this.vlmAnalysisService = vlmAnalysisService;
    }

    public RiskAssessmentResponse evaluateRisk(double latitude, double longitude, double radiusMeters, String infrastructureType) {
        VlmAnalysisResponse analysis = vlmAnalysisService.analyzeArea(latitude, longitude, radiusMeters);
        return calculateRiskScore(analysis, infrastructureType);
    }

    public RiskAssessmentResponse calculateRiskScore(VlmAnalysisResponse analysis, String infrastructureType) {
        if (analysis.getMeasurementCount() == 0) {
            return new RiskAssessmentResponse(
                    analysis.getLatitude(),
                    analysis.getLongitude(),
                    analysis.getRadiusMeters(),
                    0,
                    "LOW",
                    "No InSAR measurement points found within the requested radius.",
                    List.of(new RiskFactor("DATA_AVAILABILITY", "Insufficient InSAR sampling points in area", "LOW", 0)),
                    analysis
            );
        }

        List<RiskFactor> factors = new ArrayList<>();
        int rawScore = 0;

        // 1. Average Subsidence Rate Factor
        double avgVlm = analysis.getAverageVlmMmPerYear();
        if (avgVlm <= -5.0) {
            rawScore += 35;
            factors.add(new RiskFactor(
                    "AVERAGE_SUBSIDENCE",
                    String.format("Severe average regional subsidence rate of %.2f mm/year detected.", avgVlm),
                    "HIGH",
                    35
            ));
        } else if (avgVlm <= -2.5) {
            rawScore += 25;
            factors.add(new RiskFactor(
                    "AVERAGE_SUBSIDENCE",
                    String.format("Moderate average regional subsidence rate of %.2f mm/year detected.", avgVlm),
                    "MODERATE",
                    25
            ));
        } else if (avgVlm <= -1.0) {
            rawScore += 15;
            factors.add(new RiskFactor(
                    "AVERAGE_SUBSIDENCE",
                    String.format("Mild ground subsidence rate of %.2f mm/year observed.", avgVlm),
                    "LOW",
                    15
            ));
        } else {
            rawScore += 5;
            factors.add(new RiskFactor(
                    "AVERAGE_SUBSIDENCE",
                    String.format("Stable ground condition with average rate of %.2f mm/year.", avgVlm),
                    "LOW",
                    5
            ));
        }

        // 2. Maximum Local Sink / Peak Subsidence Factor
        double minVlm = analysis.getMinimumVlmMmPerYear();
        if (minVlm <= -10.0) {
            rawScore += 30;
            factors.add(new RiskFactor(
                    "PEAK_LOCAL_SINK",
                    String.format("Critical peak local subsidence point reaching %.2f mm/year within radius.", minVlm),
                    "HIGH",
                    30
            ));
        } else if (minVlm <= -5.0) {
            rawScore += 20;
            factors.add(new RiskFactor(
                    "PEAK_LOCAL_SINK",
                    String.format("Significant peak local subsidence point of %.2f mm/year detected.", minVlm),
                    "MODERATE",
                    20
            ));
        } else if (minVlm <= -2.0) {
            rawScore += 10;
            factors.add(new RiskFactor(
                    "PEAK_LOCAL_SINK",
                    String.format("Noticeable local peak subsidence rate of %.2f mm/year.", minVlm),
                    "LOW",
                    10
            ));
        } else {
            rawScore += 2;
            factors.add(new RiskFactor(
                    "PEAK_LOCAL_SINK",
                    "No localized peak subsidence hotspot observed.",
                    "LOW",
                    2
            ));
        }

        // 3. Spatial Extent (Proportion of negative movement points)
        double negPct = analysis.getNegativeMovementPercentage();
        if (negPct >= 80.0) {
            rawScore += 25;
            factors.add(new RiskFactor(
                    "SPATIAL_EXTENT",
                    String.format("Widespread subsidence pattern; %.1f%% of nearby measurements show negative VLM.", negPct),
                    "HIGH",
                    25
            ));
        } else if (negPct >= 50.0) {
            rawScore += 15;
            factors.add(new RiskFactor(
                    "SPATIAL_EXTENT",
                    String.format("Moderate spatial spread; %.1f%% of nearby points are experiencing downward movement.", negPct),
                    "MODERATE",
                    15
            ));
        } else {
            rawScore += 5;
            factors.add(new RiskFactor(
                    "SPATIAL_EXTENT",
                    String.format("Localized movement pattern; only %.1f%% of measurements show downward trend.", negPct),
                    "LOW",
                    5
            ));
        }

        // 4. Infrastructure Type Sensitivity Multiplier
        double multiplier = getInfrastructureMultiplier(infrastructureType);
        if (multiplier > 1.0) {
            int addedRisk = (int) Math.round(rawScore * (multiplier - 1.0));
            rawScore += addedRisk;
            factors.add(new RiskFactor(
                    "INFRASTRUCTURE_VULNERABILITY",
                    String.format("Asset category '%s' has higher structural vulnerability multiplier (%.2fx).", infrastructureType, multiplier),
                    "MODERATE",
                    addedRisk
            ));
        }

        // Final score capping
        int finalScore = Math.min(100, Math.max(0, rawScore));
        String riskLevel;
        if (finalScore >= 70) {
            riskLevel = "HIGH";
        } else if (finalScore >= 35) {
            riskLevel = "MEDIUM";
        } else {
            riskLevel = "LOW";
        }

        String summary = String.format(
                "Risk assessment evaluated as %s (Score: %d/100) based on %d InSAR data points with average VLM of %.2f mm/yr.",
                riskLevel, finalScore, analysis.getMeasurementCount(), avgVlm
        );

        return new RiskAssessmentResponse(
                analysis.getLatitude(),
                analysis.getLongitude(),
                analysis.getRadiusMeters(),
                finalScore,
                riskLevel,
                summary,
                factors,
                analysis
        );
    }

    private double getInfrastructureMultiplier(String type) {
        if (type == null) return 1.0;
        return switch (type.toUpperCase()) {
            case "METRO_STATION", "RAILWAY_STATION" -> 1.25;
            case "BRIDGE", "FLYOVER" -> 1.20;
            case "HOSPITAL", "HIGH_RISE_BUILDING" -> 1.15;
            case "ROAD", "HIGHWAY" -> 1.05;
            default -> 1.0;
        };
    }
}
