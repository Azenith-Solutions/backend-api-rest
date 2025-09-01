package com.azenithsolutions.backendapirest.v2.infrastructure.web.controller;

import com.azenithsolutions.backendapirest.v2.core.domain.model.component.EletronicComponent;
import com.azenithsolutions.backendapirest.v2.core.usecase.components.*;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.mappers.EletronicComponentMapper;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.components.CreateEletronicComponentDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.components.EletronicComponentResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eletronic-components")
public class EletronicComponentController {
    private final CreateEletronicComponentUseCase createEletronicComponentUseCase;
    private final GetEletronicComponentUseCase getEletronicComponentUseCase;
    private final GetEletronicComponentByIdUseCase getEletronicComponentByIdUseCase;
    private final DeleteEletronicComponentUseCase deleteEletronicComponentUseCase;
    private final UpdateEletronicComponentUseCase updateEletronicComponentUseCase;

    public EletronicComponentController(CreateEletronicComponentUseCase createEletronicComponentUseCase, GetEletronicComponentUseCase getEletronicComponentUseCase, GetEletronicComponentByIdUseCase getEletronicComponentByIdUseCase, DeleteEletronicComponentUseCase deleteEletronicComponentUseCase, UpdateEletronicComponentUseCase updateEletronicComponentUseCase) {
        this.createEletronicComponentUseCase = createEletronicComponentUseCase;
        this.getEletronicComponentUseCase = getEletronicComponentUseCase;
        this.getEletronicComponentByIdUseCase = getEletronicComponentByIdUseCase;
        this.deleteEletronicComponentUseCase = deleteEletronicComponentUseCase;
        this.updateEletronicComponentUseCase = updateEletronicComponentUseCase;
    }

    @PostMapping
    public ResponseEntity<EletronicComponentResponseDTO> createEletronicComponent(@RequestBody CreateEletronicComponentDTO dto) {
        EletronicComponent componente = createEletronicComponentUseCase.execute(dto);
        EletronicComponentResponseDTO responseDTO = EletronicComponentMapper.toResponseDTO(componente);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<EletronicComponentResponseDTO>> getEletronicComponent() {
        List<EletronicComponent> componentes = getEletronicComponentUseCase.execute();
        List<EletronicComponentResponseDTO> responseDTOs = EletronicComponentMapper.toResponseDTOList(componentes);
        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEletronicComponentById(@PathVariable Long id) {
        EletronicComponent componente = getEletronicComponentByIdUseCase.execute(id);
        EletronicComponentResponseDTO responseDTO = EletronicComponentMapper.toResponseDTO(componente);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEletronicComponent(@PathVariable Long id) {
        deleteEletronicComponentUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEletronicComponent( @PathVariable Long id, @RequestBody EletronicComponentResponseDTO dto) {
        EletronicComponent componente = updateEletronicComponentUseCase.execute(id, dto);
        EletronicComponentResponseDTO responseDTO = EletronicComponentMapper.toResponseDTO(componente);
        return ResponseEntity.ok(responseDTO);
    }
}
