package com.azenithsolutions.backendapirest.v1.controller.user;

import com.azenithsolutions.backendapirest.v1.dto.shared.ApiResponseDTO;
import com.azenithsolutions.backendapirest.v1.dto.user.UserListResponseDTO;
import com.azenithsolutions.backendapirest.v1.dto.user.UserUpdateRequestDTO;
import com.azenithsolutions.backendapirest.v1.dto.user.UserUpdateResponseDTO;
import com.azenithsolutions.backendapirest.v1.model.User;
import com.azenithsolutions.backendapirest.v1.service.user.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityExistsException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "User Management - v1", description = "Endpoints to manage users")
@RestController
@RequestMapping("/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<?>> getUsers( HttpServletRequest request) {
        try{
            List<User> users = userService.findAllUsers();

            List<UserListResponseDTO> userListResponseDTOS = users.stream()
                    .map(user -> new UserListResponseDTO(
                            user.getId(),
                            user.getFullName(),
                            user.getEmail(),
                            user.getFkFuncao().getFuncao(),
                            true,  // mock up
                            user.getCreatedAt()
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(
                    new ApiResponseDTO<>(
                            LocalDateTime.now(),
                            HttpStatus.OK.value(),
                            "Success",
                            userListResponseDTOS,
                            request.getRequestURI()
                    )
            );
        }catch(Exception e) {
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

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<?>> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequestDTO body, HttpServletRequest request){
        try{
            Optional<User> updatedUser = userService.updateUser(id, body);

            if (updatedUser.isPresent()) {
                User user = updatedUser.get();

                UserUpdateResponseDTO responseDTO = new UserUpdateResponseDTO(
                        user.getFullName(),
                        user.getEmail(),
                        user.getFkFuncao().getFuncao(),
                        user.getUpdatedAt()
                );

                return ResponseEntity.status(HttpStatus.OK).body(
                        new ApiResponseDTO<>(
                                LocalDateTime.now(),
                                HttpStatus.OK.value(),
                                "User updated successfully",
                                List.of(responseDTO),
                                request.getRequestURI()
                        )
                );
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponseDTO<>(
                            LocalDateTime.now(),
                            HttpStatus.NOT_FOUND.value(),
                            "User not found",
                            List.of("User with this ID does not exist"),
                            request.getRequestURI()
                    )
            );
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponseDTO<>(
                            LocalDateTime.now(),
                            HttpStatus.BAD_REQUEST.value(),
                            "Bad Request",
                            e.getMessage(),
                            request.getRequestURI()
                    )
            );
        }catch(Exception e) {
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

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<?>> deleteUser(@PathVariable Long id, HttpServletRequest request) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                    new ApiResponseDTO<>(
                            LocalDateTime.now(),
                            HttpStatus.NO_CONTENT.value(),
                            "User deleted successfully",
                            null,
                            request.getRequestURI()
                    )
            );
        }catch(EntityExistsException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponseDTO<>(
                            LocalDateTime.now(),
                            HttpStatus.NOT_FOUND.value(),
                            "User not found",
                            List.of("User with this ID does not exist"),
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
