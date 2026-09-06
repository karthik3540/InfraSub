package com.infrasub.backend.dto;

public class RiskFactor {

    private String category;
    private String description;
    private String severity;
    private int impactPoints;

    public RiskFactor() {
    }

    public RiskFactor(String category, String description, String severity, int impactPoints) {
        this.category = category;
        this.description = description;
        this.severity = severity;
        this.impactPoints = impactPoints;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getSeverity() {
        return severity;
    }

    public int getImpactPoints() {
        return impactPoints;
    }
}
