package com.infrasub.backend.dto;

public class InSARMeasurementResponse {

    private Long id;
    private Double latitude;
    private Double longitude;
    private Double vlmCmPerYear;
    private Double vlmMmPerYear;

    public InSARMeasurementResponse(
            Long id,
            Double latitude,
            Double longitude,
            Double vlmCmPerYear,
            Double vlmMmPerYear) {

        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.vlmCmPerYear = vlmCmPerYear;
        this.vlmMmPerYear = vlmMmPerYear;
    }

    public Long getId() {
        return id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getVlmCmPerYear() {
        return vlmCmPerYear;
    }

    public Double getVlmMmPerYear() {
        return vlmMmPerYear;
    }
}