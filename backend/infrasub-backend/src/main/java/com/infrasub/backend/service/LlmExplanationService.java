package com.infrasub.backend.service;

import com.infrasub.backend.dto.RiskAssessmentResponse;
import com.infrasub.backend.dto.RiskFactor;
import com.infrasub.backend.dto.VlmAnalysisResponse;
import com.infrasub.backend.entity.KnowledgeDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LlmExplanationService {

    @Value("${llm.api-key:}")
    private String apiKey;

    @Value("${llm.provider:template}")
    private String provider;

    public String generateExplanation(
            RiskAssessmentResponse riskAssessment,
            String infrastructureName,
            String infrastructureType,
            List<KnowledgeDocument> retrievedDocs) {

        VlmAnalysisResponse vlm = riskAssessment.getVlmAnalysis();
        String assetLabel = (infrastructureName != null && !infrastructureName.isBlank())
                ? infrastructureName : "Selected Infrastructure Site";

        StringBuilder sb = new StringBuilder();

        sb.append(String.format("### 📊 Evidence-Based Risk Explanation: %s\n\n", assetLabel));
        sb.append(String.format("**Assessed Risk Level:** `%s` (Numerical Score: **%d / 100**)\n\n",
                riskAssessment.getRiskLevel(), riskAssessment.getRiskScore()));

        sb.append("#### 1. Empirical Ground Movement Analysis\n");
        sb.append(String.format("Based on **%d** satellite InSAR measurements retrieved from PostGIS within a **%.0f meter** radius:\n",
                vlm.getMeasurementCount(), vlm.getRadiusMeters()));
        sb.append(String.format("- **Average Vertical Land Motion (VLM):** `%.2f mm/year`\n", vlm.getAverageVlmMmPerYear()));
        sb.append(String.format("- **Maximum Local Subsidence (Peak Sink):** `%.2f mm/year`\n", vlm.getMinimumVlmMmPerYear()));
        sb.append(String.format("- **Proportion of Subsiding Scatterers:** `%.1f%%` of nearby points\n", vlm.getNegativeMovementPercentage()));
        sb.append(String.format("- **Deformation Status:** `%s`\n\n", vlm.getDeformationStatus()));

        sb.append("#### 2. Contributing Risk Drivers\n");
        for (RiskFactor factor : riskAssessment.getRiskFactors()) {
            sb.append(String.format("- **[%s - %s]**: %s (+%d pts)\n",
                    factor.getSeverity(), factor.getCategory(), factor.getDescription(), factor.getImpactPoints()));
        }
        sb.append("\n");

        sb.append("#### 3. Synthesized Scientific & Engineering Evidence (RAG Context)\n");
        if (retrievedDocs == null || retrievedDocs.isEmpty()) {
            sb.append("No specific literature benchmarks retrieved. Evaluation relies solely on empirical VLM thresholds.\n\n");
        } else {
            for (int i = 0; i < retrievedDocs.size(); i++) {
                KnowledgeDocument doc = retrievedDocs.get(i);
                sb.append(String.format("**[%d] %s** (*Source: %s*)\n", i + 1, doc.getTitle(), doc.getSource()));
                sb.append(String.format("> \"%s\"\n\n", doc.getContent()));
            }
        }

        sb.append("#### 4. Engineering Impact & Recommendations\n");
        if ("HIGH".equalsIgnoreCase(riskAssessment.getRiskLevel())) {
            sb.append("⚠️ **High Priority Action Required**: Ground deformation rates around this site suggest active sub-surface consolidation or groundwater stress. Recommend installing ground-based tiltmeters and conducting high-precision levelling surveys.\n\n");
        } else if ("MEDIUM".equalsIgnoreCase(riskAssessment.getRiskLevel())) {
            sb.append("⚡ **Moderate Surveillance Advised**: Moderate subsidence rates observed. Periodic structural health monitoring should be integrated into scheduled maintenance cycles.\n\n");
        } else {
            sb.append("✅ **Stable Baseline**: Rates remain within standard baseline tolerances. Continue routine satellite radar monitoring.\n\n");
        }

        sb.append("#### 5. Methodological Scope & Limitations\n");
        sb.append("Numerical calculations are executed strictly by PostGIS spatial algorithms and deterministic Java risk engines. The LLM RAG layer provides non-destructive contextual literature synthesis and structural guidance.");

        return sb.toString();
    }
}
