package com.infrasub.backend.repository;

import com.infrasub.backend.entity.InSARMeasurement;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InSARMeasurementRepository
        extends JpaRepository<InSARMeasurement, Long> {

    @Query(value = """
            SELECT *
            FROM insar_measurements
            WHERE ST_DWithin(
                geom::geography,
                ST_SetSRID(
                    ST_MakePoint(:longitude, :latitude),
                    4326
                )::geography,
                :radius
            )
            """, nativeQuery = true)
    List<InSARMeasurement> findNearbyMeasurements(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("radius") double radius
    );
}