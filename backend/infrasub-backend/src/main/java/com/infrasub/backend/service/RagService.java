package com.infrasub.backend.service;

import com.infrasub.backend.dto.RiskAssessmentResponse;
import com.infrasub.backend.entity.KnowledgeDocument;
import com.infrasub.backend.repository.KnowledgeDocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RagService {

    private final KnowledgeDocumentRepository repository;

    public RagService(KnowledgeDocumentRepository repository) {
        this.repository = repository;
    }

    public List<KnowledgeDocument> retrieveRelevantContext(RiskAssessmentResponse riskAssessment, String infrastructureType, int limit) {
        String searchQuery = buildSearchQuery(riskAssessment, infrastructureType);
        List<KnowledgeDocument> docs = repository.searchByText(searchQuery, limit);
        if (docs.isEmpty()) {
            docs = repository.findAll().stream().limit(limit).toList();
        }
        return docs;
    }

    private String buildSearchQuery(RiskAssessmentResponse risk, String infraType) {
        StringBuilder sb = new StringBuilder();
        if (infraType != null && !infraType.isBlank()) {
            sb.append(infraType.toLowerCase()).append(" ");
        }
        if (risk.getVlmAnalysis() != null) {
            if (risk.getVlmAnalysis().getAverageVlmMmPerYear() < -2.0) {
                sb.append("subsidence settlement ");
            }
        }
        sb.append("groundwater deformation InSAR");
        return sb.toString().trim();
    }
}
