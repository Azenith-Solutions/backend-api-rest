package com.azenithsolutions.backendapirest.v1.controller;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.azenithsolutions.backendapirest.v1.dto.LoginRequestDTO;
import com.azenithsolutions.backendapirest.v1.dto.LoginResponseDTO;
import com.azenithsolutions.backendapirest.v1.dto.RegisterRequestDTO;
import com.azenithsolutions.backendapirest.v1.dto.RegisterResponseDTO;
import com.azenithsolutions.backendapirest.v1.model.User;
import com.azenithsolutions.backendapirest.v1.repository.UserRepository;
import com.azenithsolutions.backendapirest.v1.service.TokenService;
import com.azenithsolutions.backendapirest.v1.service.UserService;
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

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@Valid @RequestBody LoginRequestDTO body){
        try{
            User user = userService.findByUserByEmail(body.getEmail());

            if(user == null){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
            }

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
