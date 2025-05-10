package com.azenithsolutions.backendapirest.v1.controller.role;

import com.azenithsolutions.backendapirest.v1.dto.role.RoleListDTO;
import com.azenithsolutions.backendapirest.v1.dto.shared.ApiResponseDTO;
import com.azenithsolutions.backendapirest.v1.dto.user.UserListDTO;
import com.azenithsolutions.backendapirest.v1.model.Role;
import com.azenithsolutions.backendapirest.v1.model.User;
import com.azenithsolutions.backendapirest.v1.service.role.RoleService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<?>> getRoles( HttpServletRequest request) {
        try{
            List<Role> roles = roleService.findAllRoles();

            List<RoleListDTO> roleListDTOs = roles.stream()
                    .map(role -> new RoleListDTO(
                            role.getIdFuncao(),
                            role.getFuncao()
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(
                    new ApiResponseDTO<>(
                            LocalDateTime.now(),
                            HttpStatus.OK.value(),
                            "Success",
                            roleListDTOs,
                            request.getRequestURI()
                    )
            );
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponseDTO<>(
                            LocalDateTime.now(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Internal Server Error",
                            List.of("An error occurred while processing the request"),
                            request.getRequestURI()
                    )
            );
        }
    }
}
