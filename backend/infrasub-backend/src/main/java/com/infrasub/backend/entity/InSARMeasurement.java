package com.infrasub.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "insar_measurements")
public class InSARMeasurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double latitude;

    private Double longitude;

    @Column(name = "vlm_cm_per_year")
    private Double vlmCmPerYear;

    @Column(name = "vlm_mm_per_year")
    private Double vlmMmPerYear;

    public InSARMeasurement() {
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