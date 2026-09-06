package com.infrasub.backend.controller;

import com.infrasub.backend.dto.InfrastructureResponse;
import com.infrasub.backend.dto.InfrastructureRiskResponse;
import com.infrasub.backend.dto.RiskAssessmentResponse;
import com.infrasub.backend.entity.KnowledgeDocument;
import com.infrasub.backend.service.LlmExplanationService;
import com.infrasub.backend.service.RagService;
import com.infrasub.backend.service.RiskAssessmentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/assessment")
public class AssessmentController {

    private final RiskAssessmentService riskAssessmentService;
    private final RagService ragService;
    private final LlmExplanationService llmExplanationService;

    public AssessmentController(
            RiskAssessmentService riskAssessmentService,
            RagService ragService,
            LlmExplanationService llmExplanationService) {

        this.riskAssessmentService = riskAssessmentService;
        this.ragService = ragService;
        this.llmExplanationService = llmExplanationService;
    }

    @GetMapping("/evaluate")
    public InfrastructureRiskResponse evaluateLocation(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "1000") double radius,
            @RequestParam(required = false, defaultValue = "Custom Location") String name,
            @RequestParam(required = false, defaultValue = "OTHER") String type) {

        RiskAssessmentResponse riskResponse = riskAssessmentService.evaluateRisk(latitude, longitude, radius, type);
        List<KnowledgeDocument> docs = ragService.retrieveRelevantContext(riskResponse, type, 3);
        String explanation = llmExplanationService.generateExplanation(riskResponse, name, type, docs);

        InfrastructureResponse dummyInfra = new InfrastructureResponse(0L, name, type, latitude, longitude, "Ad-hoc spatial risk analysis point");

        return new InfrastructureRiskResponse(
                dummyInfra,
                riskResponse.getVlmAnalysis(),
                riskResponse,
                explanation
        );
    }
}
