package com.infrasub.backend.dto;

import java.util.List;

public class RiskAssessmentResponse {

    private double latitude;
    private double longitude;
    private double radiusMeters;
    private int riskScore;
    private String riskLevel;
    private String summary;
    private List<RiskFactor> riskFactors;
    private VlmAnalysisResponse vlmAnalysis;

    public RiskAssessmentResponse() {
    }

    public RiskAssessmentResponse(
            double latitude,
            double longitude,
            double radiusMeters,
            int riskScore,
            String riskLevel,
            String summary,
            List<RiskFactor> riskFactors,
            VlmAnalysisResponse vlmAnalysis) {

        this.latitude = latitude;
        this.longitude = longitude;
        this.radiusMeters = radiusMeters;
        this.riskScore = riskScore;
        this.riskLevel = riskLevel;
        this.summary = summary;
        this.riskFactors = riskFactors;
        this.vlmAnalysis = vlmAnalysis;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getRadiusMeters() {
        return radiusMeters;
    }

    public int getRiskScore() {
        return riskScore;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public String getSummary() {
        return summary;
    }

    public List<RiskFactor> getRiskFactors() {
        return riskFactors;
    }

    public VlmAnalysisResponse getVlmAnalysis() {
        return vlmAnalysis;
    }
}
