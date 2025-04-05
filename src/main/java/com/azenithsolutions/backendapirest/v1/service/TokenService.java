package com.azenithsolutions.backendapirest.v1.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.azenithsolutions.backendapirest.v1.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.web.OffsetScrollPositionHandlerMethodArgumentResolver;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    private final OffsetScrollPositionHandlerMethodArgumentResolver offsetResolver;
    @Value("${api.security.token.secret}")
    private String secret;

    public TokenService(OffsetScrollPositionHandlerMethodArgumentResolver offsetResolver) {
        this.offsetResolver = offsetResolver;
    }

    public String generateToken(User user) {
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("backend-api-rest")
                    .withSubject(user.getEmail())
                    .withExpiresAt(this.generateExpirationDate())
                    .sign(algorithm);
        }catch (JWTCreationException e){
            throw new JWTCreationException("Error while creating JWT",e);
        }
    }

    public String subjectToken(String token) {
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    .withIssuer("backend-api-rest")
                    .build()
                    .verify(token)
                    .getSubject();
        }catch (JWTVerificationException e){
            return null;
        }
    }

    private Instant generateExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }


    public boolean isTokenValid(String token) {
        if(token == null || token.isEmpty() || this.subjectToken(token) == null){
            return false;
        }
        return true;
    }
}
