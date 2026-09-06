package com.infrasub.backend.repository;

import com.infrasub.backend.entity.Infrastructure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InfrastructureRepository extends JpaRepository<Infrastructure, Long> {

    List<Infrastructure> findByType(String type);

    List<Infrastructure> findByNameContainingIgnoreCase(String name);
}
