package com.infrasub.backend.dto;

public class VlmAnalysisResponse {

    private double latitude;
    private double longitude;
    private double radiusMeters;
    private int measurementCount;
    private double averageVlmMmPerYear;
    private double minimumVlmMmPerYear;
    private double maximumVlmMmPerYear;
    private double negativeMovementPercentage;
    private String deformationStatus;

    public VlmAnalysisResponse() {
    }

    public VlmAnalysisResponse(
            double latitude,
            double longitude,
            double radiusMeters,
            int measurementCount,
            double averageVlmMmPerYear,
            double minimumVlmMmPerYear,
            double maximumVlmMmPerYear,
            double negativeMovementPercentage,
            String deformationStatus) {

        this.latitude = latitude;
        this.longitude = longitude;
        this.radiusMeters = radiusMeters;
        this.measurementCount = measurementCount;
        this.averageVlmMmPerYear = averageVlmMmPerYear;
        this.minimumVlmMmPerYear = minimumVlmMmPerYear;
        this.maximumVlmMmPerYear = maximumVlmMmPerYear;
        this.negativeMovementPercentage = negativeMovementPercentage;
        this.deformationStatus = deformationStatus;
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

    public int getMeasurementCount() {
        return measurementCount;
    }

    public double getAverageVlmMmPerYear() {
        return averageVlmMmPerYear;
    }

    public double getMinimumVlmMmPerYear() {
        return minimumVlmMmPerYear;
    }

    public double getMaximumVlmMmPerYear() {
        return maximumVlmMmPerYear;
    }

    public double getNegativeMovementPercentage() {
        return negativeMovementPercentage;
    }

    public String getDeformationStatus() {
        return deformationStatus;
    }
}
