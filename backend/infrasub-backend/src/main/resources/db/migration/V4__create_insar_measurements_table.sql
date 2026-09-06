CREATE TABLE insar_measurements (
                                    id BIGSERIAL PRIMARY KEY,

                                    latitude DOUBLE PRECISION NOT NULL,
                                    longitude DOUBLE PRECISION NOT NULL,

                                    vlm_cm_per_year DOUBLE PRECISION NOT NULL,
                                    vlm_mm_per_year DOUBLE PRECISION NOT NULL,

                                    geom geometry(Point, 4326) NOT NULL
);

CREATE INDEX idx_insar_measurements_geom
    ON insar_measurements
    USING GIST (geom);