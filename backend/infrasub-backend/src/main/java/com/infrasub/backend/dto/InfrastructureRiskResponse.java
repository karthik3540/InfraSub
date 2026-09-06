package com.infrasub.backend.dto;

public class InfrastructureRiskResponse {

    private InfrastructureResponse infrastructure;
    private VlmAnalysisResponse analysis;
    private RiskAssessmentResponse risk;
    private String explanation;

    public InfrastructureRiskResponse() {
    }

    public InfrastructureRiskResponse(
            InfrastructureResponse infrastructure,
            VlmAnalysisResponse analysis,
            RiskAssessmentResponse risk,
            String explanation) {

        this.infrastructure = infrastructure;
        this.analysis = analysis;
        this.risk = risk;
        this.explanation = explanation;
    }

    public InfrastructureResponse getInfrastructure() {
        return infrastructure;
    }

    public VlmAnalysisResponse getAnalysis() {
        return analysis;
    }

    public RiskAssessmentResponse getRisk() {
        return risk;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
