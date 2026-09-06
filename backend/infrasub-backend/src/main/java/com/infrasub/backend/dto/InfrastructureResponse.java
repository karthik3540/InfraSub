package com.infrasub.backend.dto;

public class InfrastructureResponse {

    private Long id;
    private String name;
    private String type;
    private Double latitude;
    private Double longitude;
    private String description;

    public InfrastructureResponse() {
    }

    public InfrastructureResponse(Long id, String name, String type, Double latitude, Double longitude, String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getDescription() {
        return description;
    }
}
