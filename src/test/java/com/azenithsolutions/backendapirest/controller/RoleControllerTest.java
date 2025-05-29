package com.azenithsolutions.backendapirest.controller;

import com.azenithsolutions.backendapirest.v1.dto.role.RoleListDTO;
import com.azenithsolutions.backendapirest.v1.dto.shared.ApiResponseDTO;
import com.azenithsolutions.backendapirest.v1.model.Role;
import com.azenithsolutions.backendapirest.v1.controller.role.RoleController;
import com.azenithsolutions.backendapirest.v1.model.User;
import com.azenithsolutions.backendapirest.v1.service.role.RoleService;


import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RoleControllerTest {

    @Mock
    private RoleService roleService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private RoleController roleController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetRoles_ReturnsSuccess() {
            // Arrange
            List<User> usersAdmin = new ArrayList<>();
            usersAdmin.add(new User(
                    1,
                    "João Victor",
                    "joao.victor@example.com",
                    "senhaSegura123",
                    "https://example.com/fotos/joao.jpg",
                    true,
                    null,
                    LocalDate.now(),
                    LocalDate.now()
            ));

            List<User> usersUser = new ArrayList<>();
            usersUser.add(new User(
                    2,
                    "Mariana Silva",
                    "maria.silva@example.com",
                    "outraSenha123",
                    "https://example.com/fotos/maria.jpg",
                    true,
                    null,
                    LocalDate.now(),
                    LocalDate.now()
            ));

            Role role1 = new Role(1L, "ADMIN", usersAdmin);
            Role role2 = new Role(2L, "USER", usersUser);

            when(roleService.findAllRoles()).thenReturn(List.of(role1, role2));
            when(request.getRequestURI()).thenReturn("/roles");

            // Act
            ResponseEntity<ApiResponseDTO<?>> response = roleController.getRoles(request);

            // Assert
            assertEquals(200, response.getStatusCodeValue());
            ApiResponseDTO<?> body = response.getBody();
            assertNotNull(body);
            assertEquals("Success", body.getMessage());

            List<RoleListDTO> data = (List<RoleListDTO>) body.getData();
            assertEquals(2, data.size());
            assertEquals("ADMIN", data.get(0).funcao());
            assertEquals("USER", data.get(1).funcao());
        }


    @Test
    public void testGetRoles_InternalServerError() {
        // Arrange
        when(roleService.findAllRoles()).thenThrow(new RuntimeException("DB error"));
        when(request.getRequestURI()).thenReturn("/roles");

        // Act
        ResponseEntity<ApiResponseDTO<?>> response = roleController.getRoles(request);

        // Assert
        assertEquals(500, response.getStatusCodeValue());
        ApiResponseDTO<?> body = response.getBody();
        assertNotNull(body);
        assertEquals("Internal Server Error", body.getMessage());
        assertEquals("/roles", body.getPath());
    }
}
