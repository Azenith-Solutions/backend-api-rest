package com.azenithsolutions.backendapirest.v1.controller;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.azenithsolutions.backendapirest.v1.dto.LoginRequestDTO;
import com.azenithsolutions.backendapirest.v1.dto.LoginResponseDTO;
import com.azenithsolutions.backendapirest.v1.dto.RegisterRequestDTO;
import com.azenithsolutions.backendapirest.v1.dto.RegisterResponseDTO;
import com.azenithsolutions.backendapirest.v1.model.User;
import com.azenithsolutions.backendapirest.v1.service.TokenService;
import com.azenithsolutions.backendapirest.v1.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication - v1", description = "Endpoints to authenticate with JWT Token validation")
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Operation(summary = "Sign in", description = "User sign in validation")
    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@Valid @RequestBody LoginRequestDTO body){
        try{
            System.out.println("Email: " + body.getEmail());
            System.out.println("Senha: " + body.getPassword()); // Verifique se a senha está aqui!

            User user = userService.findUserByEmail(body.getEmail());

            System.out.println("body: " + body);
            System.out.println("user: " + user);

            if (passwordEncoder.matches(body.getPassword(), user.getPassword())) {
                String token = this.tokenService.generateToken(user);

                return ResponseEntity.status(HttpStatus.OK).body(new LoginResponseDTO(
                        user.getEmail(),
                        token
                ));
            }else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
            }
        }catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch(JWTCreationException e ){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }

    @Operation(summary = "Sign up", description = "User sign up validation")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequestDTO body){
        try{
            User user = new User();
            user.setFullName(body.getFullName());
            user.setEmail(body.getEmail());
            user.setPassword(passwordEncoder.encode(body.getPassword()));

            userService.register(user);

            String token = this.tokenService.generateToken(user);

            return ResponseEntity.status(HttpStatus.OK).body(new RegisterResponseDTO(
                    user.getEmail(),
                    token
            ));
        }catch(EntityExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }
}
