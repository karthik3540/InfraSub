package com.infrasub.backend.service;

import com.infrasub.backend.dto.HealthResponse;
import org.springframework.stereotype.Service;

@Service
public class HealthService {
    public HealthResponse getHealth(){
        return new HealthResponse("UP", "infrasub-backend");
    }
}
