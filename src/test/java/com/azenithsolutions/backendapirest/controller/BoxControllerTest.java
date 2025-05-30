package com.azenithsolutions.backendapirest.controller;

import com.azenithsolutions.backendapirest.v1.controller.Box.BoxController;
import com.azenithsolutions.backendapirest.v1.dto.shared.ApiResponseDTO;
import com.azenithsolutions.backendapirest.v1.model.Box;
import com.azenithsolutions.backendapirest.v1.service.Box.BoxService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

public class BoxControllerTest {

    @Mock
    private BoxService boxService;

    @InjectMocks
    private BoxController boxController;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve retornar sucesso 200 ao listar as caixas")
    void testGetBoxes_success() {
        Box box1 = new Box();
        box1.setIdCaixa(1L);
        box1.setNomeCaixa("Caixa A");

        Box box2 = new Box();
        box2.setIdCaixa(2L);
        box2.setNomeCaixa("Caixa B");

        when(boxService.findAllBoxes()).thenReturn(List.of(box1, box2));
        when(request.getRequestURI()).thenReturn("/v1/boxes");

        ResponseEntity<ApiResponseDTO<?>> response = boxController.getBoxes(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Success", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
    }

    @Test
    @DisplayName("Deve retornar erro 500 ao listar as caixas")
    void testGetBoxes_error() {
        when(boxService.findAllBoxes()).thenThrow(new RuntimeException("Erro interno"));
        when(request.getRequestURI()).thenReturn("/v1/boxes");

        ResponseEntity<ApiResponseDTO<?>> response = boxController.getBoxes(request);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Internal Server Error", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
    }
}
