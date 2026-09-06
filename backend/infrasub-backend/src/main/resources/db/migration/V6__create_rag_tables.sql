CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE knowledge_documents (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    source VARCHAR(255) NOT NULL,
    category VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    embedding vector(384),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_knowledge_documents_embedding
ON knowledge_documents
USING ivfflat (embedding vector_cosine_ops)
WITH (lists = 10);

-- Seed scientific literature and engineering guidelines for RAG context
INSERT INTO knowledge_documents (title, source, category, content) VALUES
(
    'Coastal Groundwater Depletion and InSAR-Observed Subsidence in Chennai Basin',
    'Journal of Coastal Hydrogeology & Remote Sensing (2024)',
    'CHENNAI_SUBSIDENCE',
    'Groundwater extraction in coastal sedimentary formations around Chennai causes pore-pressure decline and consolidation in fine-grained alluvial clay layers. Sentinel-1 InSAR measurements confirm localized subsidence rates ranging from -2.0 mm/yr to -12.5 mm/yr in dense urban corridors. Fine-grained clay deposits show heightened compaction sensitivity during dry seasons.'
),
(
    'Sentinel-1 Persistent Scatterer Interferometry Guidelines for Urban Deformation Monitoring',
    'European Remote Sensing Standards (2023)',
    'INSAR_METHODOLOGY',
    'Persistent Scatterer Interferometry (PSI) processes temporal sequences of C-band SAR images to measure Vertical Land Motion (VLM) with millimeter-per-year precision. Negative VLM values signify downward ground displacement (subsidence), while positive values indicate ground heave or tectonic uplift. Subsidence rates below -3.0 mm/yr denote active ground deformation needing continuous structural surveillance.'
),
(
    'Geotechnical Risk Assessment and Settlement Criteria for Underground Urban Transit Systems',
    'Urban Infrastructure Engineering Handbook (2022)',
    'INFRASTRUCTURE_RISK',
    'Subsurface transit infrastructure, including underground metro stations, utility vaults, and tunnel linings, are highly sensitive to differential ground movement. Differential settlement exceeding 1:500 angular distortion or ground subsidence rates exceeding -5.0 mm/yr introduce risk of micro-cracking, joint displacement, and water ingress in structural concrete elements.'
),
(
    'Structural Assessment of Highway Bridges and Elevated Corridors under Soft Soil Settlement',
    'Bridge & Structural Mechanics Review (2024)',
    'GEOTECHNICAL_GUIDELINES',
    'Deep pier foundations of elevated bridges in estuarine and deltaic soils are susceptible to negative skin friction when surrounding soil undergoes active consolidation. Continuous subsidence greater than -4.0 mm/yr surrounding bridge abutments increases risk of differential pier movement, bearing misalignment, and long-term expansion joint failure.'
);
