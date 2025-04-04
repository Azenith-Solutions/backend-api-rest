package com.azenithsolutions.backendapirest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        try{
            http
                    .authorizeHttpRequests((authorize) -> authorize
                            .requestMatchers("/api/v1/**").permitAll()
                            .requestMatchers("/swagger-ui/**", "/v1/api-docs/**").authenticated()
                            .anyRequest().permitAll()
                    ).httpBasic(httpBasic -> {})
                    .csrf(csrf -> csrf.disable());

            return http.build();
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.builder()
                .username("root")
                .password(passwordEncoder().encode("root123"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}