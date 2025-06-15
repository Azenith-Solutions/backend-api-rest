package com.azenithsolutions.backendapirest.controller;

import com.azenithsolutions.backendapirest.v1.controller.component.ComponentController;
import com.azenithsolutions.backendapirest.v1.dto.component.ComponentRequestDTO;
import com.azenithsolutions.backendapirest.v1.model.enums.ComponentCondition;

import com.azenithsolutions.backendapirest.v1.dto.shared.ApiResponseDTO;
import com.azenithsolutions.backendapirest.v1.model.Component;
import com.azenithsolutions.backendapirest.v1.service.component.ComponentService;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ComponentControllerTest {

    @Mock
    private ComponentService componentService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private ComponentController componentController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllComponents_ReturnsSuccess() {
        // Arrange
        Component component = new Component();
        component.setIdComponente(1L);
        component.setDescricao("Processador Ryzen 5");
        component.setCreatedAt(LocalDate.now());
        component.setUpdatedAt(LocalDate.now());

        when(componentService.getAllComponents()).thenReturn(List.of(component));
        when(request.getRequestURI()).thenReturn("/components");

        // Act
        ResponseEntity<ApiResponseDTO<?>> response = componentController.getAllComponents(request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        ApiResponseDTO<?> body = response.getBody();
        assertNotNull(body);
        assertEquals("OK", body.getMessage());
        assertEquals("/components", body.getPath());

        List<Component> data = (List<Component>) body.getData();
        assertEquals(1, data.size());
        assertEquals("Processador Ryzen 5", data.get(0).getDescricao());
    }

    @Test
    public void testGetAllComponents_ReturnsEmptyList() {
        // Arrange
        when(componentService.getAllComponents()).thenReturn(List.of());
        when(request.getRequestURI()).thenReturn("/components");

        // Act
        ResponseEntity<ApiResponseDTO<?>> response = componentController.getAllComponents(request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        ApiResponseDTO<?> body = response.getBody();
        assertNotNull(body);
        assertEquals("OK", body.getMessage());
        assertEquals("/components", body.getPath());

        List<Component> data = (List<Component>) body.getData();
        assertTrue(data.isEmpty());
    }

    @Test
    public void testGetAllComponents_InternalServerError() {
        // Arrange
        when(componentService.getAllComponents()).thenThrow(new RuntimeException("Erro no banco"));
        when(request.getRequestURI()).thenReturn("/components");

        // Act
        ResponseEntity<ApiResponseDTO<?>> response = componentController.getAllComponents(request);

        // Assert
        assertEquals(500, response.getStatusCodeValue());
        ApiResponseDTO<?> body = response.getBody();
        assertNotNull(body);
        assertEquals("Erro interno: Erro no banco", body.getMessage());
        assertEquals("/components", body.getPath());
    }

    @Test
    public void testGetComponentById_ReturnsSuccess() {
        // Arrange
        Long componentId = 1L;
        Component component = new Component();
        component.setIdComponente(componentId);
        component.setDescricao("Memória RAM 16GB");
        component.setCreatedAt(LocalDate.now());
        component.setUpdatedAt(LocalDate.now());

        when(componentService.findById(componentId)).thenReturn(Optional.of(component));
        when(request.getRequestURI()).thenReturn("/components/" + componentId);

        // Act
        ResponseEntity<ApiResponseDTO<?>> response = componentController.getComponentById(componentId, request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        ApiResponseDTO<?> body = response.getBody();
        assertNotNull(body);
        assertEquals("OK", body.getMessage());
        assertEquals("/components/1", body.getPath());

        Component returnedComponent = (Component) body.getData();
        assertEquals("Memória RAM 16GB", returnedComponent.getDescricao());
    }

    @Test
    public void testGetComponentById_NotFound() {
        // Arrange
        Long componentId = 999L;
        when(componentService.findById(componentId)).thenReturn(Optional.empty());
        when(request.getRequestURI()).thenReturn("/components/" + componentId);

        // Act
        ResponseEntity<ApiResponseDTO<?>> response = componentController.getComponentById(componentId, request);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        ApiResponseDTO<?> body = response.getBody();
        assertNotNull(body);
        assertEquals("Componente não encontrado!", body.getMessage());
        assertEquals("/components/999", body.getPath());
    }

    @Test
    void testCreateComponent_ReturnsCreated() {
        // Arrange
        ComponentRequestDTO dto = createSampleComponentRequestDTO();
        MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());

        Component savedComponent = new Component();
        savedComponent.setIdComponente(1L);
        savedComponent.setIdHardWareTech(dto.getIdHardWareTech());
        savedComponent.setDescricao(dto.getDescricao());

        // Update the mock to match the correct method signature (without MultipartFile)
        when(componentService.save(any(ComponentRequestDTO.class))).thenReturn(savedComponent);
        when(request.getRequestURI()).thenReturn("/components");

        // Act
        ResponseEntity<ApiResponseDTO<?>> response = componentController.createComponent(dto, file, request);

        // Assert
        assertEquals(201, response.getStatusCodeValue());
        ApiResponseDTO<?> body = response.getBody();
        assertNotNull(body);
        assertEquals("Componente cadastrado!", body.getMessage());
        assertEquals("/components", body.getPath());

        Component result = (Component) body.getData();
        assertEquals("HW123", result.getIdHardWareTech());
        assertEquals("Descrição do componente", result.getDescricao());
    }

    @Test
    void testCreateComponent_ThrowsException_ReturnsInternalServerError() {
        // Arrange
        ComponentRequestDTO dto = createSampleComponentRequestDTO();
        MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());

        // Update the mock to match the correct method signature (without MultipartFile)
        when(componentService.save(any(ComponentRequestDTO.class)))
                .thenThrow(new RuntimeException("Erro ao salvar componente"));

        when(request.getRequestURI()).thenReturn("/components");

        // Act
        ResponseEntity<ApiResponseDTO<?>> response = componentController.createComponent(dto, file, request);

        // Assert
        assertEquals(500, response.getStatusCodeValue());
        ApiResponseDTO<?> body = response.getBody();
        assertNotNull(body);
        assertEquals("Erro interno: Erro ao salvar componente", body.getMessage());
        assertEquals("/components", body.getPath());
        assertNull(body.getData());
    }

    @Test
    void testUpdateComponent_ReturnsOk() {
        // Arrange
        Long id = 1L;
        ComponentRequestDTO dto = createUpdatedComponentRequestDTO();
        MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());

        Component updatedComponent = new Component();
        updatedComponent.setIdComponente(id);
        updatedComponent.setIdHardWareTech(dto.getIdHardWareTech());
        updatedComponent.setDescricao(dto.getDescricao());

        when(componentService.update(eq(id), any(ComponentRequestDTO.class))).thenReturn(updatedComponent);
        when(request.getRequestURI()).thenReturn("/components/" + id);

        // Act
        ResponseEntity<ApiResponseDTO<?>> response = componentController.updateComponent(id, dto, file, request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        ApiResponseDTO<?> body = response.getBody();
        assertNotNull(body);
        assertEquals("Componente atualizado!", body.getMessage());
        assertEquals("/components/1", body.getPath());

        Component result = (Component) body.getData();
        assertEquals("HW123-EDITADO", result.getIdHardWareTech());
        assertEquals("Componente atualizado", result.getDescricao());
    }

    @Test
    void testUpdateComponent_ComponentNotFound() {
        // Arrange
        Long id = 999L;
        ComponentRequestDTO dto = createUpdatedComponentRequestDTO();
        MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());

        when(componentService.update(eq(id), any(ComponentRequestDTO.class)))
                .thenThrow(new RuntimeException("Componente não encontrado"));

        when(request.getRequestURI()).thenReturn("/components/" + id);

        // Act
        ResponseEntity<ApiResponseDTO<?>> response = componentController.updateComponent(id, dto, file, request);

        // Assert
        assertEquals(500, response.getStatusCodeValue());
        ApiResponseDTO<?> body = response.getBody();
        assertNotNull(body);
        assertEquals("Erro interno: Componente não encontrado", body.getMessage());
        assertEquals("/components/999", body.getPath());
        assertNull(body.getData());
    }

    @Test
    void testUpdateComponent_ThrowsException_ReturnsInternalServerError() {
        // Arrange
        Long id = 1L;
        ComponentRequestDTO dto = createSampleComponentRequestDTO();
        MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());

        when(componentService.update(eq(id), any(ComponentRequestDTO.class)))
                .thenThrow(new RuntimeException("Erro ao atualizar componente"));

        when(request.getRequestURI()).thenReturn("/components/" + id);

        // Act
        ResponseEntity<ApiResponseDTO<?>> response = componentController.updateComponent(id, dto, file, request);

        // Assert
        assertEquals(500, response.getStatusCodeValue());
        ApiResponseDTO<?> body = response.getBody();
        assertNotNull(body);
        assertEquals("Erro interno: Erro ao atualizar componente", body.getMessage());
        assertEquals("/components/1", body.getPath());
        assertNull(body.getData());
    }

    @Test
    void testDeleteComponent_ReturnsOk() {
        // Arrange
        Long id = 1L;
        Component mockComponent = new Component();
        mockComponent.setIdComponente(id);

        when(componentService.findById(id)).thenReturn(Optional.of(mockComponent));
        doNothing().when(componentService).delete(id);
        when(request.getRequestURI()).thenReturn("/components/" + id);

        // Act
        ResponseEntity<ApiResponseDTO<?>> response = componentController.deleteComponent(id, request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Componente deletado!", response.getBody().getMessage());
        assertEquals("/components/" + id, response.getBody().getPath());
        verify(componentService, times(1)).delete(id);
    }

    @Test
    void testDeleteComponent_ComponentNotFound() {
        // Arrange
        Long id = 999L;
        when(componentService.findById(id)).thenReturn(Optional.empty());
        when(request.getRequestURI()).thenReturn("/components/" + id);

        // Act
        ResponseEntity<ApiResponseDTO<?>> response = componentController.deleteComponent(id, request);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Componente não encontrado!", response.getBody().getMessage());
        assertEquals("/components/" + id, response.getBody().getPath());
        verify(componentService, never()).delete(id);
    }

    @Test
    void testDeleteComponent_WhenDeleteThrowsException_ReturnsInternalServerError() {
        // Arrange
        Long id = 1L;
        Component mockComponent = new Component();
        mockComponent.setIdComponente(id);

        when(componentService.findById(id)).thenReturn(Optional.of(mockComponent));
        doThrow(new RuntimeException("Erro inesperado")).when(componentService).delete(id);
        when(request.getRequestURI()).thenReturn("/components/" + id);

        // Act
        ResponseEntity<ApiResponseDTO<?>> response = componentController.deleteComponent(id, request);

        // Assert
        assertEquals(500, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMessage().contains("Erro interno"));
        assertEquals("/components/" + id, response.getBody().getPath());
    }

    // Helper methods to create sample DTOs
    private ComponentRequestDTO createSampleComponentRequestDTO() {
        ComponentRequestDTO dto = new ComponentRequestDTO();
        dto.setIdHardWareTech("HW123");
        dto.setFkCaixa(1L);
        dto.setFkCategoria(1L);
        dto.setPartNumber("PN123");
        dto.setQuantidade(10);
        dto.setFlagML(true);
        dto.setCodigoML("COD123");
        dto.setFlagVerificado(true);
        dto.setCondicao(ComponentCondition.BOM_ESTADO);
        dto.setObservacao("Observação de teste");
        dto.setDescricao("Descrição do componente");
        return dto;
    }

    private ComponentRequestDTO createUpdatedComponentRequestDTO() {
        ComponentRequestDTO dto = new ComponentRequestDTO();
        dto.setIdHardWareTech("HW123-EDITADO");
        dto.setFkCaixa(1L);
        dto.setFkCategoria(1L);
        dto.setPartNumber("PN123-EDITADO");
        dto.setQuantidade(5);
        dto.setFlagML(false);
        dto.setCodigoML("COD456");
        dto.setFlagVerificado(false);
        dto.setCondicao(ComponentCondition.EM_OBSERVACAO);
        dto.setObservacao("Obs atualizada");
        dto.setDescricao("Componente atualizado");
        return dto;
    }
}

