package com.azenithsolutions.backendapirest.v1.controller.component;

import com.azenithsolutions.backendapirest.v1.dto.component.ComponentCatalogResponseDTO;
import com.azenithsolutions.backendapirest.v1.dto.component.ComponentObservationDTO;
import com.azenithsolutions.backendapirest.v1.dto.component.ComponentRequestDTO;
import com.azenithsolutions.backendapirest.v1.dto.component.ComponentVisibilityDTO;
import com.azenithsolutions.backendapirest.v1.dto.shared.ApiResponseDTO;
import com.azenithsolutions.backendapirest.v1.model.Component;
import com.azenithsolutions.backendapirest.v1.service.component.ComponentService;
import com.azenithsolutions.backendapirest.v1.service.files.ImageService;
import com.azenithsolutions.backendapirest.v1.utils.CustomMultipartFile;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
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

@Tag(name = "Component Management - v1", description = "Endpoints to manage components")
@RestController
@RequestMapping("/v1/components")
public class ComponentController {

    @Autowired
    private ComponentService componentService;

    @Autowired
    private ImageService imageService;

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

    @GetMapping("/catalog")
    public ResponseEntity<ApiResponseDTO<?>> getPagebleComponentsCatalog(HttpServletRequest request, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String nomeComponente, @RequestParam(required = false) Long categoria) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
            Page<ComponentCatalogResponseDTO> pagina = componentService.getPagebleComponents(pageable, nomeComponente, categoria);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.OK.value(),
                                    "OK",
                                    pagina,
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
    public ResponseEntity<ApiResponseDTO<?>> getFilterComponentList(HttpServletRequest request, @RequestBody(required = false) HashMap<String, Object> filtros) {
        try {
            List<ComponentCatalogResponseDTO> components = componentService.getFilterComponentList(filtros);

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

    @GetMapping("/details/{id}")
    public ResponseEntity<ApiResponseDTO<?>> getDetailsComponentById(@PathVariable Long id, HttpServletRequest request) {
        try {
            ComponentCatalogResponseDTO component = componentService.findDetailsComponentById(id);

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

    @GetMapping("/kpi/in-observation")
    public ResponseEntity<ApiResponseDTO<?>> getInObservationComponents(HttpServletRequest request) {
        try {
            List<ComponentObservationDTO> components = componentService.getInObservationComponents();

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

    @GetMapping("/kpi/out-of-last-sale-sla")
    public ResponseEntity<ApiResponseDTO<?>> getOutOfLastSaleComponents(HttpServletRequest request) {
        try {
            List<Component> components = componentService.getComponentsOutOfLastSaleSLA();

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

    @GetMapping("/dashboard/flag-ml")
    public ResponseEntity<ApiResponseDTO<?>> getCountOfTrueAndFalseFlagML(HttpServletRequest request) {
        try {
            List<Integer> componentsWithFlagMLTrueAndFalse = componentService.getCountOfTrueAndFalseFlagML();

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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseDTO<?>> createComponent(
            @RequestPart(value = "data", required = false) ComponentRequestDTO componentData,
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

            if (file != null) {
                System.out.println("Arquivo: " + file.getOriginalFilename() + " | Tamanho: " + file.getSize());
            }

            if (file != null && !file.isEmpty()) {
                String originalFilename = file.getOriginalFilename();
                String timestamp = String.valueOf(System.currentTimeMillis());
                String uniqueFileName = timestamp + "_" + originalFilename;

                MultipartFile renamedFile = new CustomMultipartFile(file, uniqueFileName);


                componentData.setImagem(renamedFile.getOriginalFilename());

                String fileName = imageService.saveImage(renamedFile);
                System.out.println("File saved with name: " + fileName);
            }

            Component createdComponent = componentService.save(componentData);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.CREATED.value(),
                                    "Componente cadastrado com sucesso!",
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

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseDTO<?>> updateComponent(
            @PathVariable Long id,
            @RequestPart(value = "data") ComponentRequestDTO componentData,
            @RequestPart(value = "file", required = false) MultipartFile file,
            HttpServletRequest request) {
        try {
            Optional<Component> existingComponentOpt = componentService.findById(id);

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

            // Handle image if provided
            if (file != null && !file.isEmpty()) {
                String originalFilename = file.getOriginalFilename();
                String timestamp = String.valueOf(System.currentTimeMillis());
                String uniqueFileName = timestamp + "_" + originalFilename;

                MultipartFile renamedFile = new CustomMultipartFile(file, uniqueFileName);

                // Save image
                String fileName = imageService.saveImage(renamedFile);

                // Set the image filename in the component data
                componentData.setImagem(renamedFile.getOriginalFilename());
            } else {
                // Maintain the existing image if no new image is provided
                Component existingComponent = existingComponentOpt.get();
                if (existingComponent.getImagem() != null && !existingComponent.getImagem().isEmpty()) {
                    componentData.setImagem(existingComponent.getImagem());
                }
            }

            Component updatedComponent = componentService.update(id, componentData);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.OK.value(),
                                    "Componente atualizado com sucesso!",
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

    @PutMapping("/{id}/visibility")
    public ResponseEntity<ApiResponseDTO<?>> updateVisibilityWithPut(
            @PathVariable Long id,
            @RequestBody ComponentVisibilityDTO visibilityDTO,
            HttpServletRequest request) {
        return updateComponentVisibility(id, visibilityDTO, request);
    }

    @PatchMapping("/{id}/visibility")
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

            Component updatedComponent = componentService.updateVisibility(id, visibilityDTO.getIsVisibleCatalog());

            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.OK.value(),
                                    "Visibilidade do componente atualizada no catálogo com sucesso!",
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

    @PostMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseDTO<?>> uploadComponentImage(
            @PathVariable Long id,
            @RequestParam("image") MultipartFile image,
            HttpServletRequest request) {
        try {
            Optional<Component> componentOpt = componentService.findById(id);

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

            Component component = componentOpt.get();

            String fileName = imageService.saveImage(image);

            ComponentRequestDTO updateDTO = new ComponentRequestDTO();
            updateDTO.setIdHardWareTech(component.getIdHardWareTech());
            updateDTO.setNomeComponente(component.getNomeComponente());
            updateDTO.setFkCaixa(component.getFkCaixa().getIdCaixa());
            updateDTO.setFkCategoria(component.getFkCategoria().getIdCategoria());
            updateDTO.setPartNumber(component.getPartNumber());
            updateDTO.setQuantidade(component.getQuantidade());
            updateDTO.setFlagML(component.getFlagML());
            updateDTO.setCodigoML(component.getCodigoML());
            updateDTO.setFlagVerificado(component.getFlagVerificado());
            updateDTO.setCondicao(component.getCondicao());
            updateDTO.setObservacao(component.getObservacao());
            updateDTO.setDescricao(component.getDescricao());
            updateDTO.setImagem(fileName);

            Component updatedComponent = componentService.update(id, updateDTO);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.OK.value(),
                                    "Imagem do componente atualizada com sucesso!",
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
}
