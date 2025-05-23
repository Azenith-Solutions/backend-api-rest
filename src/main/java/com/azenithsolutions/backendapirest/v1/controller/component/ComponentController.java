package com.azenithsolutions.backendapirest.v1.controller.component;

import com.azenithsolutions.backendapirest.v1.dto.component.ComponentRequestDTO;
import com.azenithsolutions.backendapirest.v1.dto.shared.ApiResponseDTO;
import com.azenithsolutions.backendapirest.v1.model.Component;
import com.azenithsolutions.backendapirest.v1.service.component.ComponentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Component Management - v1", description = "Endpoints to manage components")
@RestController
@RequestMapping("/v1/components")
public class ComponentController {
    @Autowired
    private ComponentService componentService;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<?>> getAllComponents(HttpServletRequest request) {
        try {
            List<Component> components = componentService.getAllComponents();

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

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<?>> getComponentById(@PathVariable Long id, HttpServletRequest request) {
        try {
            Component component = componentService.findById(id).orElse(null);

            if (component == null) {
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

            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.OK.value(),
                                    "OK",
                                    component,
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
    public ResponseEntity<ApiResponseDTO<?>> getLowStockComponents(HttpServletRequest request) {
        try {
            List<Component> lowStockComponents = componentService.getLowStockComponents();

            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.OK.value(),
                                    "OK",
                                    lowStockComponents,
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

    @GetMapping("/kpi/critics-and-observations")
    public ResponseEntity<ApiResponseDTO<?>> getInObservationComponents(HttpServletRequest request) {
        try {
            List<Component> components = componentService.getInObservationComponents();

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

    @GetMapping("/kpi/incompletes")
    public ResponseEntity<ApiResponseDTO<?>> getIncompleteComponents(HttpServletRequest request) {
        try {
            List<Component> incompleteComponents = componentService.getIncompleteComponents();

            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.OK.value(),
                                    "OK",
                                    incompleteComponents,
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

    @PostMapping
    public ResponseEntity<ApiResponseDTO<?>> createComponent(@Valid @RequestBody ComponentRequestDTO componentRequestDTO, HttpServletRequest request) {
        try {
            Component createdComponent = componentService.save(componentRequestDTO);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.CREATED.value(),
                                    "Componente cadastrado!",
                                    createdComponent,
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

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<?>> updateComponent(@PathVariable Long id, @Valid @RequestBody ComponentRequestDTO componentRequestDTO, HttpServletRequest request) {
        try {
            Component updatedComponent = componentService.update(id, componentRequestDTO);

            if (updatedComponent == null) {
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

            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.OK.value(),
                                    "Componente atualizado!",
                                    updatedComponent,
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
    public ResponseEntity<ApiResponseDTO<?>> deleteComponent(@PathVariable Long id, HttpServletRequest request) {
        try {
            Component existingComponent = componentService.findById(id).orElse(null);

            if (existingComponent == null) {
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

            componentService.delete(id);

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
}
