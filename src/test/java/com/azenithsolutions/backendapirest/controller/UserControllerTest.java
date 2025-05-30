package com.azenithsolutions.backendapirest.controller;

import com.azenithsolutions.backendapirest.v1.controller.user.UserController;
import com.azenithsolutions.backendapirest.v1.dto.shared.ApiResponseDTO;
import com.azenithsolutions.backendapirest.v1.dto.user.UserUpdateRequestDTO;
import com.azenithsolutions.backendapirest.v1.model.Role;
import com.azenithsolutions.backendapirest.v1.model.User;
import com.azenithsolutions.backendapirest.v1.service.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUsers_ReturnSuccess() {
        User user = new User(1, "João Silva", "joao@example.com", "senha123", null, true,
                new Role(1L, "Admin", null), LocalDate.now(), LocalDate.now());

        when(userService.findAllUsers()).thenReturn(List.of(user));
        when(request.getRequestURI()).thenReturn("/v1/users");

        ResponseEntity<ApiResponseDTO<?>> response = userController.getUsers(request);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getMessage());
    }

    @Test
    public void testUpdateUser_NotFound() {
        Long id = 99L;
        UserUpdateRequestDTO dto = new UserUpdateRequestDTO();

        when(userService.updateUser(eq(id), any())).thenReturn(Optional.empty());
        when(request.getRequestURI()).thenReturn("/v1/users/99");

        ResponseEntity<ApiResponseDTO<?>> response = userController.updateUser(id, dto, request);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Not Found", response.getBody().getMessage());
    }

    @Test
    public void testDeleteUser_ReturnNoContent() {
        Long id = 1L;
        doNothing().when(userService).deleteUser(id);
        when(request.getRequestURI()).thenReturn("/v1/users/1");

        ResponseEntity<ApiResponseDTO<?>> response = userController.deleteUser(id, request);

        assertEquals(204, response.getStatusCodeValue());
        assertEquals("No Content", response.getBody().getMessage());
    }
}
