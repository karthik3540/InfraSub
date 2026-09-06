package com.infrasub.backend.service;

import com.infrasub.backend.dto.InfrastructureRequest;
import com.infrasub.backend.dto.InfrastructureResponse;
import com.infrasub.backend.dto.InfrastructureRiskResponse;
import com.infrasub.backend.dto.RiskAssessmentResponse;
import com.infrasub.backend.entity.Infrastructure;
import com.infrasub.backend.entity.KnowledgeDocument;
import com.infrasub.backend.repository.InfrastructureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InfrastructureService {

    private final InfrastructureRepository repository;
    private final RiskAssessmentService riskAssessmentService;
    private final RagService ragService;
    private final LlmExplanationService llmExplanationService;

    public InfrastructureService(
            InfrastructureRepository repository,
            RiskAssessmentService riskAssessmentService,
            RagService ragService,
            LlmExplanationService llmExplanationService) {

        this.repository = repository;
        this.riskAssessmentService = riskAssessmentService;
        this.ragService = ragService;
        this.llmExplanationService = llmExplanationService;
    }

    @Transactional(readOnly = true)
    public List<InfrastructureResponse> getAllInfrastructure() {
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InfrastructureResponse getInfrastructureById(Long id) {
        Infrastructure infra = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Infrastructure asset not found with ID: " + id));
        return mapToResponse(infra);
    }

    public InfrastructureResponse createInfrastructure(InfrastructureRequest request) {
        Infrastructure infra = new Infrastructure(
                request.getName(),
                request.getType(),
                request.getLatitude(),
                request.getLongitude(),
                request.getDescription()
        );
        Infrastructure saved = repository.save(infra);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public InfrastructureRiskResponse getInfrastructureRisk(Long id, double radiusMeters) {
        Infrastructure infra = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Infrastructure asset not found with ID: " + id));

        InfrastructureResponse infraDto = mapToResponse(infra);
        RiskAssessmentResponse riskResponse = riskAssessmentService.evaluateRisk(
                infra.getLatitude(),
                infra.getLongitude(),
                radiusMeters,
                infra.getType()
        );

        List<KnowledgeDocument> retrievedDocs = ragService.retrieveRelevantContext(riskResponse, infra.getType(), 3);
        String explanation = llmExplanationService.generateExplanation(
                riskResponse,
                infra.getName(),
                infra.getType(),
                retrievedDocs
        );

        return new InfrastructureRiskResponse(
                infraDto,
                riskResponse.getVlmAnalysis(),
                riskResponse,
                explanation
        );
    }

    private InfrastructureResponse mapToResponse(Infrastructure infra) {
        return new InfrastructureResponse(
                infra.getId(),
                infra.getName(),
                infra.getType(),
                infra.getLatitude(),
                infra.getLongitude(),
                infra.getDescription()
        );
    }
}
