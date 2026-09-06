CREATE TABLE infrastructure (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(100) NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    description TEXT,
    geom geometry(Point, 4326),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_infrastructure_geom
ON infrastructure
USING GIST (geom);

-- Seed prominent Chennai infrastructure assets
INSERT INTO infrastructure (name, type, latitude, longitude, description, geom) VALUES
('Chennai Central Metro Station', 'METRO_STATION', 13.0827, 80.2757, 'Major underground interchange subway transit hub in central Chennai', ST_SetSRID(ST_MakePoint(80.2757, 13.0827), 4326)),
('Kathipara Junction Flyover', 'BRIDGE', 13.0067, 80.2025, 'Cloverleaf flyover junction connecting Grand Southern Trunk Road and Anna Salai', ST_SetSRID(ST_MakePoint(80.2025, 13.0067), 4326)),
('Anna Salai Bridge & Corridor', 'BRIDGE', 13.0603, 80.2496, 'Key arterial bridge spanning Cooum River along Anna Salai corridor', ST_SetSRID(ST_MakePoint(80.2496, 13.0603), 4326)),
('Chennai Port Commercial Terminal', 'BUILDING', 13.0839, 80.2925, 'Major artificial harbor infrastructure and container freight complex', ST_SetSRID(ST_MakePoint(80.2925, 13.0839), 4326)),
('Rajiv Gandhi Govt General Hospital', 'HOSPITAL', 13.0812, 80.2778, 'Multi-specialty public healthcare facility adjacent to Park Town', ST_SetSRID(ST_MakePoint(80.2778, 13.0812), 4326)),
('Koyambedu Metro & Bus Hub', 'METRO_STATION', 13.0694, 80.1948, 'Intercity bus terminal and elevated Metro line hub', ST_SetSRID(ST_MakePoint(80.1948, 13.0694), 4326));
