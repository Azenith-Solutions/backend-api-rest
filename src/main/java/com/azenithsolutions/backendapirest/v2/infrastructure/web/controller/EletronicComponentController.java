package com.azenithsolutions.backendapirest.v2.infrastructure.web.controller;

import com.azenithsolutions.backendapirest.v2.core.domain.model.component.EletronicComponent;
import com.azenithsolutions.backendapirest.v2.core.usecase.components.*;
import com.azenithsolutions.backendapirest.v2.core.usecase.components.command.CreateEletronicComponentCommand;
import com.azenithsolutions.backendapirest.v2.core.usecase.components.command.UpdateEletronicComponentCommand;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.components.EletronicComponentResponseDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.mappers.EletronicComponentMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/eletronic-components")
@Tag(name = "Electronic Component Management", description = "Clean architecture endpoint for Electronic Components")
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
    @Operation(summary = "Create electronic component", description = "Creates a new electronic component")
    public ResponseEntity<Map<String, Object>> createEletronicComponent(@RequestBody CreateEletronicComponentCommand command) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            EletronicComponent componente = createEletronicComponentUseCase.execute(command);
            
            EletronicComponentResponseDTO responseDTO = EletronicComponentMapper.toResponseDTO(componente);
            
            response.put("message", "Componente eletrônico criado com sucesso!");
            response.put("component", responseDTO);
            
            return ResponseEntity.status(201).body(response);
        } catch (IllegalArgumentException exception) {
            response.put("message", "Argumento Inválido: %s".formatted(exception.getMessage()));
            return ResponseEntity.status(400).body(response);
        } catch (RuntimeException exception) {
            response.put("message", "Erro: %s".formatted(exception.getMessage()));
            return ResponseEntity.status(400).body(response);
        }
    }

    @GetMapping
    @Operation(summary = "List electronic components", description = "Returns all electronic components")
    public ResponseEntity<List<EletronicComponentResponseDTO>> getEletronicComponent() {
        List<EletronicComponent> componentes = getEletronicComponentUseCase.execute();
        List<EletronicComponentResponseDTO> responseDTOs = EletronicComponentMapper.toResponseDTOList(componentes);
        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get electronic component by id", description = "Returns electronic component by id")
    public ResponseEntity<Map<String, Object>> getEletronicComponentById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<EletronicComponent> componente = getEletronicComponentByIdUseCase.execute(id);
            if (!componente.isPresent()) {
                response.put("message", "Componente eletrônico não encontrado!");
                return ResponseEntity.status(404).body(response);
            }
            
            EletronicComponentResponseDTO responseDTO = EletronicComponentMapper.toResponseDTO(componente.get());
            
            response.put("message", "Componente eletrônico encontrado!");
            response.put("component", responseDTO);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException exception) {
            response.put("message", "Erro: %s".formatted(exception.getMessage()));
            return ResponseEntity.status(400).body(response);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete electronic component", description = "Deletes an electronic component by id")
    public ResponseEntity<Map<String, Object>> deleteEletronicComponent(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            deleteEletronicComponentUseCase.execute(id);
            response.put("message", "Componente eletrônico excluído com sucesso!");
            return ResponseEntity.status(200).body(response);
        } catch (RuntimeException exception) {
            response.put("message", "Erro: %s".formatted(exception.getMessage()));
            return ResponseEntity.status(400).body(response);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update electronic component", description = "Updates an existing electronic component")
    public ResponseEntity<Map<String, Object>> updateEletronicComponent(@PathVariable Long id, @RequestBody UpdateEletronicComponentCommand command) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            EletronicComponent componente = updateEletronicComponentUseCase.execute(id, command);
            
            EletronicComponentResponseDTO responseDTO = EletronicComponentMapper.toResponseDTO(componente);
            
            response.put("message", "Componente eletrônico atualizado com sucesso!");
            response.put("component", responseDTO);
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException exception) {
            response.put("message", "Argumento Inválido: %s".formatted(exception.getMessage()));
            return ResponseEntity.status(400).body(response);
        } catch (RuntimeException exception) {
            response.put("message", "Erro: %s".formatted(exception.getMessage()));
            return ResponseEntity.status(400).body(response);
        }
    }
}
