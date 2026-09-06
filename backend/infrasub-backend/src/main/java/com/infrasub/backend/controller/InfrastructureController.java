package com.infrasub.backend.controller;

import com.infrasub.backend.dto.InfrastructureRequest;
import com.infrasub.backend.dto.InfrastructureResponse;
import com.infrasub.backend.dto.InfrastructureRiskResponse;
import com.infrasub.backend.service.InfrastructureService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/infrastructure")
public class InfrastructureController {

    private final InfrastructureService service;

    public InfrastructureController(InfrastructureService service) {
        this.service = service;
    }

    @GetMapping
    public List<InfrastructureResponse> getAllInfrastructure() {
        return service.getAllInfrastructure();
    }

    @GetMapping("/{id}")
    public InfrastructureResponse getInfrastructureById(@PathVariable Long id) {
        return service.getInfrastructureById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InfrastructureResponse createInfrastructure(@RequestBody InfrastructureRequest request) {
        return service.createInfrastructure(request);
    }

    @GetMapping("/{id}/risk")
    public InfrastructureRiskResponse getInfrastructureRisk(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1000") double radius) {

        return service.getInfrastructureRisk(id, radius);
    }
}
