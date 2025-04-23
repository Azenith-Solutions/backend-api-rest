package com.azenithsolutions.backendapirest.v1.controller.component;

import com.azenithsolutions.backendapirest.v1.dto.component.ComponentResponseDTO;
import com.azenithsolutions.backendapirest.v1.service.component.ComponentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name= "Component Management - v1", description = "Endpoints to manage components")
@RestController
@RequestMapping("/v1/components")
public class ComponentController {
    @Autowired
    private ComponentService componentService;

    @GetMapping
    public ResponseEntity<List<ComponentResponseDTO>> getAllComponents() {
        try {
            List<ComponentResponseDTO> components = componentService.getAllComponents();
            return ResponseEntity.status(HttpStatus.OK).body(components);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
