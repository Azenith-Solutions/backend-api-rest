package com.azenithsolutions.backendapirest.v2.infrastructure.web.controller;

import com.azenithsolutions.backendapirest.v2.core.domain.model.component.EletronicComponent;
import com.azenithsolutions.backendapirest.v2.core.usecase.components.*;
import com.azenithsolutions.backendapirest.v2.core.usecase.components.command.*;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.components.ComponentObservationDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.components.ComponentVisibilityDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.components.EletronicComponentResponseDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.shared.ApiResponseDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.mappers.EletronicComponentMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v2/components")
@Tag(name = "Electronic Component Management - V2", description = "Clean architecture endpoint for Electronic Components")
public class EletronicComponentController {
    private final CreateEletronicComponentUseCase createEletronicComponentUseCase;
    private final GetEletronicComponentUseCase getEletronicComponentUseCase;
    private final GetEletronicComponentByIdUseCase getEletronicComponentByIdUseCase;
    private final DeleteEletronicComponentUseCase deleteEletronicComponentUseCase;
    private final UpdateEletronicComponentUseCase updateEletronicComponentUseCase;
    
    private final GetComponentCatalogUseCase getComponentCatalogUseCase;
    private final GetFilterComponentsUseCase getFilterComponentsUseCase;
    private final GetComponentDetailsUseCase getComponentDetailsUseCase;
    private final GetLowStockComponentsUseCase getLowStockComponentsUseCase;
    private final GetComponentsInObservationUseCase getComponentsInObservationUseCase;
    private final GetIncompleteComponentsUseCase getIncompleteComponentsUseCase;
    private final GetComponentsOutOfLastSaleSLAUseCase getComponentsOutOfLastSaleSLAUseCase;
    private final GetCountOfTrueAndFalseFlagMLUseCase getCountOfTrueAndFalseFlagMLUseCase;
    private final UpdateComponentVisibilityUseCase updateComponentVisibilityUseCase;
    private final UploadComponentImageUseCase uploadComponentImageUseCase;
    private final CreateComponentWithFileUseCase createComponentWithFileUseCase;
    private final UpdateComponentWithFileUseCase updateComponentWithFileUseCase;

    public EletronicComponentController(
            CreateEletronicComponentUseCase createEletronicComponentUseCase,
            GetEletronicComponentUseCase getEletronicComponentUseCase,
            GetEletronicComponentByIdUseCase getEletronicComponentByIdUseCase,
            DeleteEletronicComponentUseCase deleteEletronicComponentUseCase,
            UpdateEletronicComponentUseCase updateEletronicComponentUseCase,
            GetComponentCatalogUseCase getComponentCatalogUseCase,
            GetFilterComponentsUseCase getFilterComponentsUseCase,
            GetComponentDetailsUseCase getComponentDetailsUseCase,
            GetLowStockComponentsUseCase getLowStockComponentsUseCase,
            GetComponentsInObservationUseCase getComponentsInObservationUseCase,
            GetIncompleteComponentsUseCase getIncompleteComponentsUseCase,
            GetComponentsOutOfLastSaleSLAUseCase getComponentsOutOfLastSaleSLAUseCase,
            GetCountOfTrueAndFalseFlagMLUseCase getCountOfTrueAndFalseFlagMLUseCase,
            UpdateComponentVisibilityUseCase updateComponentVisibilityUseCase,
            UploadComponentImageUseCase uploadComponentImageUseCase,
            CreateComponentWithFileUseCase createComponentWithFileUseCase,
            UpdateComponentWithFileUseCase updateComponentWithFileUseCase) {
        this.createEletronicComponentUseCase = createEletronicComponentUseCase;
        this.getEletronicComponentUseCase = getEletronicComponentUseCase;
        this.getEletronicComponentByIdUseCase = getEletronicComponentByIdUseCase;
        this.deleteEletronicComponentUseCase = deleteEletronicComponentUseCase;
        this.updateEletronicComponentUseCase = updateEletronicComponentUseCase;
        this.getComponentCatalogUseCase = getComponentCatalogUseCase;
        this.getFilterComponentsUseCase = getFilterComponentsUseCase;
        this.getComponentDetailsUseCase = getComponentDetailsUseCase;
        this.getLowStockComponentsUseCase = getLowStockComponentsUseCase;
        this.getComponentsInObservationUseCase = getComponentsInObservationUseCase;
        this.getIncompleteComponentsUseCase = getIncompleteComponentsUseCase;
        this.getComponentsOutOfLastSaleSLAUseCase = getComponentsOutOfLastSaleSLAUseCase;
        this.getCountOfTrueAndFalseFlagMLUseCase = getCountOfTrueAndFalseFlagMLUseCase;
        this.updateComponentVisibilityUseCase = updateComponentVisibilityUseCase;
        this.uploadComponentImageUseCase = uploadComponentImageUseCase;
        this.createComponentWithFileUseCase = createComponentWithFileUseCase;
        this.updateComponentWithFileUseCase = updateComponentWithFileUseCase;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create electronic component", description = "Creates a new electronic component")
    public ResponseEntity<ApiResponseDTO<?>> createEletronicComponent(@RequestBody CreateEletronicComponentCommand command, HttpServletRequest request) {
        try {
            EletronicComponent createdComponent = createEletronicComponentUseCase.execute(command);
            EletronicComponentResponseDTO responseDTO = EletronicComponentMapper.toResponseDTO(createdComponent);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.CREATED.value(),
                                    "Componente cadastrado com sucesso!",
                                    responseDTO,
                                    request.getRequestURI()
                            )
                    );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                    "Erro interno: " + e.getMessage(),
                                    null,
                                    request.getRequestURI()
                            )
                    );
        }
    }
    
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create electronic component with image", description = "Creates a new electronic component with image")
    public ResponseEntity<ApiResponseDTO<?>> createComponentWithFile(
            @RequestPart(value = "data", required = false) CreateEletronicComponentCommand componentData,
            @RequestPart(value = "file", required = false) MultipartFile file,
            HttpServletRequest request) {
        try {
            if (componentData == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(
                                new ApiResponseDTO<>(
                                        LocalDateTime.now(),
                                        HttpStatus.BAD_REQUEST.value(),
                                        "Bad Request",
                                        List.of("Invalid JSON format"),
                                        request.getRequestURI()
                                )
                        );
            }
            
            // In a real implementation, you would save the image and get its path
            String imagePath = null;
            if (file != null && !file.isEmpty()) {
                imagePath = "sample-path/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            }
            
            CreateComponentWithFileCommand command = new CreateComponentWithFileCommand(
                    componentData.nome(),
                    componentData.categoria(),
                    componentData.partNumber(),
                    componentData.quantidade(),
                    file
            );
            
            EletronicComponent createdComponent = createComponentWithFileUseCase.execute(command, imagePath);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.CREATED.value(),
                                    "Componente cadastrado com sucesso!",
                                    EletronicComponentMapper.toResponseDTO(createdComponent),
                                    request.getRequestURI()
                            )
                    );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                    "Erro interno: " + e.getMessage(),
                                    null,
                                    request.getRequestURI()
                            )
                    );
        }
    }

    @GetMapping
    @Operation(summary = "List electronic components", description = "Returns all electronic components")
    public ResponseEntity<ApiResponseDTO<?>> getEletronicComponent(HttpServletRequest request) {
        try {
            List<EletronicComponent> components = getEletronicComponentUseCase.execute();
            List<EletronicComponentResponseDTO> responseDTOs = EletronicComponentMapper.toResponseDTOList(components);
            
            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.OK.value(),
                                    "OK",
                                    responseDTOs,
                                    request.getRequestURI()
                            )
                    );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                    "Erro interno: " + e.getMessage(),
                                    null,
                                    request.getRequestURI()
                            )
                    );
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get electronic component by id", description = "Returns electronic component by id")
    public ResponseEntity<ApiResponseDTO<?>> getEletronicComponentById(@PathVariable Long id, HttpServletRequest request) {
        try {
            Optional<EletronicComponent> component = getEletronicComponentByIdUseCase.execute(id);
            
            if (component.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(
                                new ApiResponseDTO<>(
                                        LocalDateTime.now(),
                                        HttpStatus.NOT_FOUND.value(),
                                        "Componente não encontrado!",
                                        null,
                                        request.getRequestURI()
                                )
                        );
            }
            
            EletronicComponentResponseDTO responseDTO = EletronicComponentMapper.toResponseDTO(component.get());
            
            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.OK.value(),
                                    "OK",
                                    responseDTO,
                                    request.getRequestURI()
                            )
                    );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                    "Erro interno: " + e.getMessage(),
                                    null,
                                    request.getRequestURI()
                            )
                    );
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete electronic component", description = "Deletes an electronic component by id")
    public ResponseEntity<ApiResponseDTO<?>> deleteEletronicComponent(@PathVariable Long id, HttpServletRequest request) {
        try {
            Optional<EletronicComponent> existingComponent = getEletronicComponentByIdUseCase.execute(id);

            if (existingComponent.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(
                                new ApiResponseDTO<>(
                                        LocalDateTime.now(),
                                        HttpStatus.NOT_FOUND.value(),
                                        "Componente não encontrado!",
                                        null,
                                        request.getRequestURI()
                                )
                        );
            }
            
            deleteEletronicComponentUseCase.execute(id);
            
            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.OK.value(),
                                    "Componente deletado!",
                                    null,
                                    request.getRequestURI()
                            )
                    );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                    "Erro interno: " + e.getMessage(),
                                    null,
                                    request.getRequestURI()
                            )
                    );
        }
    }
    
    @GetMapping("/kpi/low-stock")
    @Operation(summary = "Get low stock electronic components", description = "Returns low stock electronic components")
    public ResponseEntity<ApiResponseDTO<?>> getLowStockComponents(HttpServletRequest request) {
        try {
            List<EletronicComponent> lowStockComponents = getLowStockComponentsUseCase.execute();

            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.OK.value(),
                                    "OK",
                                    EletronicComponentMapper.toResponseDTOList(lowStockComponents),
                                    request.getRequestURI()
                            )
                    );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                    "Erro interno: " + e.getMessage(),
                                    null,
                                    request.getRequestURI()
                            )
                    );
        }
    }

    @GetMapping("/kpi/in-observation")
    @Operation(summary = "Get electronic components in observation", description = "Returns electronic components in observation")
    public ResponseEntity<ApiResponseDTO<?>> getInObservationComponents(HttpServletRequest request) {
        try {
            List<ComponentObservationDTO> components = getComponentsInObservationUseCase.execute();

            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.OK.value(),
                                    "OK",
                                    components,
                                    request.getRequestURI()
                            )
                    );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                    "Erro interno: " + e.getMessage(),
                                    null,
                                    request.getRequestURI()
                            )
                    );
        }
    }

    @GetMapping("/kpi/incomplete")
    @Operation(summary = "Get incomplete electronic components", description = "Returns incomplete electronic components")
    public ResponseEntity<ApiResponseDTO<?>> getIncompleteComponents(HttpServletRequest request) {
        try {
            List<EletronicComponent> incompleteComponents = getIncompleteComponentsUseCase.execute();

            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.OK.value(),
                                    "OK",
                                    EletronicComponentMapper.toResponseDTOList(incompleteComponents),
                                    request.getRequestURI()
                            )
                    );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                    "Erro interno: " + e.getMessage(),
                                    null,
                                    request.getRequestURI()
                            )
                    );
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update electronic component", description = "Updates an existing electronic component")
    public ResponseEntity<ApiResponseDTO<?>> updateEletronicComponent(@PathVariable Long id, @RequestBody UpdateEletronicComponentCommand command, HttpServletRequest request) {
        try {
            Optional<EletronicComponent> existingComponentOpt = getEletronicComponentByIdUseCase.execute(id);

            if (existingComponentOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(
                                new ApiResponseDTO<>(
                                        LocalDateTime.now(),
                                        HttpStatus.NOT_FOUND.value(),
                                        "Componente não encontrado!",
                                        null,
                                        request.getRequestURI()
                                )
                        );
            }
            
            EletronicComponent updatedComponent = updateEletronicComponentUseCase.execute(id, command);
            
            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.OK.value(),
                                    "Componente atualizado com sucesso!",
                                    EletronicComponentMapper.toResponseDTO(updatedComponent),
                                    request.getRequestURI()
                            )
                    );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                    "Erro interno: " + e.getMessage(),
                                    null,
                                    request.getRequestURI()
                            )
                    );
        }
    }
    
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update electronic component with file", description = "Updates an existing electronic component with file")
    public ResponseEntity<ApiResponseDTO<?>> updateComponentWithFile(
            @PathVariable Long id,
            @RequestPart(value = "data") UpdateEletronicComponentCommand componentData,
            @RequestPart(value = "file", required = false) MultipartFile file,
            HttpServletRequest request) {
        try {
            Optional<EletronicComponent> existingComponentOpt = getEletronicComponentByIdUseCase.execute(id);

            if (existingComponentOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(
                                new ApiResponseDTO<>(
                                        LocalDateTime.now(),
                                        HttpStatus.NOT_FOUND.value(),
                                        "Componente não encontrado!",
                                        null,
                                        request.getRequestURI()
                                )
                        );
            }
            
            // In a real implementation, you would save the image and get its path
            String imagePath = null;
            if (file != null && !file.isEmpty()) {
                imagePath = "sample-path/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            } else {
                // Keep the existing image
                imagePath = existingComponentOpt.get().getS3ImagePath();
            }
            
            UpdateComponentWithFileCommand command = new UpdateComponentWithFileCommand(
                    componentData.nome(),
                    componentData.categoria(),
                    componentData.partNumber(),
                    componentData.quantidade(),
                    file
            );
            
            EletronicComponent updatedComponent = updateComponentWithFileUseCase.execute(id, command, imagePath);
            
            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.OK.value(),
                                    "Componente atualizado com sucesso!",
                                    EletronicComponentMapper.toResponseDTO(updatedComponent),
                                    request.getRequestURI()
                            )
                    );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                    "Erro interno: " + e.getMessage(),
                                    null,
                                    request.getRequestURI()
                            )
                    );
        }
    }
    
    @GetMapping("/catalog")
    @Operation(summary = "Get pageable electronic component catalog", description = "Returns pageable electronic component catalog")
    public ResponseEntity<ApiResponseDTO<?>> getPagebleComponentsCatalog(
            HttpServletRequest request, 
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "10") int size, 
            @RequestParam(required = false) String nomeComponente, 
            @RequestParam(required = false) Long categoria) {
        
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
            Page<EletronicComponent> pageResult = getComponentCatalogUseCase.execute(pageable, nomeComponente, categoria);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.OK.value(),
                                    "OK",
                                    pageResult,
                                    request.getRequestURI()
                            )
                    );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                    "Erro interno: " + e.getMessage(),
                                    null,
                                    request.getRequestURI()
                            )
                    );
        }
    }

    @PostMapping("/filterComponentList")
    @Operation(summary = "Filter electronic components", description = "Returns filtered electronic components")
    public ResponseEntity<ApiResponseDTO<?>> getFilterComponentList(
            HttpServletRequest request, 
            @RequestBody(required = false) HashMap<String, Object> filtros) {
        
        try {
            List<EletronicComponent> components = getFilterComponentsUseCase.execute(new FilterComponentCommand(filtros));

            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.OK.value(),
                                    "OK",
                                    EletronicComponentMapper.toResponseDTOList(components),
                                    request.getRequestURI()
                            )
                    );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                    "Erro interno: " + e.getMessage(),
                                    null,
                                    request.getRequestURI()
                            )
                    );
        }
    }

    @GetMapping("/details/{id}")
    @Operation(summary = "Get electronic component details by id", description = "Returns electronic component details by id")
    public ResponseEntity<ApiResponseDTO<?>> getDetailsComponentById(@PathVariable Long id, HttpServletRequest request) {
        try {
            Optional<EletronicComponent> componentOpt = getComponentDetailsUseCase.execute(id);

            if (componentOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(
                                new ApiResponseDTO<>(
                                        LocalDateTime.now(),
                                        HttpStatus.NOT_FOUND.value(),
                                        "Componente não encontrado!",
                                        null,
                                        request.getRequestURI()
                                )
                        );
            }

            EletronicComponentResponseDTO responseDTO = EletronicComponentMapper.toResponseDTO(componentOpt.get());

            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.OK.value(),
                                    "OK",
                                    responseDTO,
                                    request.getRequestURI()
                            )
                    );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                    "Erro interno: " + e.getMessage(),
                                    null,
                                    request.getRequestURI()
                            )
                    );
        }
    }
    
    @GetMapping("/kpi/out-of-last-sale-sla")
    @Operation(summary = "Get electronic components out of last sale SLA", description = "Returns electronic components out of last sale SLA")
    public ResponseEntity<ApiResponseDTO<?>> getOutOfLastSaleComponents(HttpServletRequest request) {
        try {
            List<EletronicComponent> components = getComponentsOutOfLastSaleSLAUseCase.execute();

            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.OK.value(),
                                    "OK",
                                    EletronicComponentMapper.toResponseDTOList(components),
                                    request.getRequestURI()
                            )
                    );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                    "Erro interno: " + e.getMessage(),
                                    null,
                                    request.getRequestURI()
                            )
                    );
        }
    }

    @GetMapping("/dashboard/flag-ml")
    @Operation(summary = "Get count of true and false flag ML", description = "Returns count of true and false flag ML")
    public ResponseEntity<ApiResponseDTO<?>> getCountOfTrueAndFalseFlagML(HttpServletRequest request) {
        try {
            List<Integer> componentsWithFlagMLTrueAndFalse = getCountOfTrueAndFalseFlagMLUseCase.execute();

            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.OK.value(),
                                    "OK",
                                    componentsWithFlagMLTrueAndFalse,
                                    request.getRequestURI()
                            )
                    );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                    "Erro interno: " + e.getMessage(),
                                    null,
                                    request.getRequestURI()
                            )
                    );
        }
    }

    @PutMapping("/{id}/visibility")
    @Operation(summary = "Update electronic component visibility", description = "Updates electronic component visibility")
    public ResponseEntity<ApiResponseDTO<?>> updateVisibilityWithPut(
            @PathVariable Long id,
            @RequestBody ComponentVisibilityDTO visibilityDTO,
            HttpServletRequest request) {
        return updateComponentVisibility(id, visibilityDTO, request);
    }

    @PatchMapping("/{id}/visibility")
    @Operation(summary = "Update electronic component visibility", description = "Updates electronic component visibility")
    public ResponseEntity<ApiResponseDTO<?>> updateVisibilityWithPatch(
            @PathVariable Long id,
            @RequestBody ComponentVisibilityDTO visibilityDTO,
            HttpServletRequest request) {
        return updateComponentVisibility(id, visibilityDTO, request);
    }

    private ResponseEntity<ApiResponseDTO<?>> updateComponentVisibility(
            Long id,
            ComponentVisibilityDTO visibilityDTO,
            HttpServletRequest request) {

        try {
            EletronicComponent updatedComponent = updateComponentVisibilityUseCase.execute(
                    id, 
                    new UpdateComponentVisibilityCommand(visibilityDTO.getIsVisibleCatalog())
            );

            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.OK.value(),
                                    "Visibilidade do componente atualizada no catálogo com sucesso!",
                                    EletronicComponentMapper.toResponseDTO(updatedComponent),
                                    request.getRequestURI()
                            )
                    );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                    "Erro interno: " + e.getMessage(),
                                    null,
                                    request.getRequestURI()
                            )
                    );
        }
    }

    @PostMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload electronic component image", description = "Uploads electronic component image")
    public ResponseEntity<ApiResponseDTO<?>> uploadComponentImage(
            @PathVariable Long id,
            @RequestParam("image") MultipartFile image,
            HttpServletRequest request) {
        try {
            // In a real implementation, you would save the image and get its path
            String imagePath = "sample-path/" + System.currentTimeMillis() + "_" + image.getOriginalFilename();
            
            EletronicComponent updatedComponent = uploadComponentImageUseCase.execute(
                    id, 
                    new UploadComponentImageCommand(image),
                    imagePath
            );

            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.OK.value(),
                                    "Imagem do componente atualizada com sucesso!",
                                    EletronicComponentMapper.toResponseDTO(updatedComponent),
                                    request.getRequestURI()
                            )
                    );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                    "Erro interno: " + e.getMessage(),
                                    null,
                                    request.getRequestURI()
                            )
                    );
        }
    }
}
