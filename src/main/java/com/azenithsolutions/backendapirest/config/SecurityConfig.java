package com.azenithsolutions.backendapirest.config;

import com.azenithsolutions.backendapirest.v1.security.FilterSecurity;
import com.azenithsolutions.backendapirest.v1.service.auth.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private FilterSecurity filterSecurity;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/emails/send").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/orders").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/items").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/components/catalog").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/categorys").permitAll()
                        .requestMatchers(HttpMethod.GET, "/uploads/images/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/images/info/*").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/components/filterComponentList").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/emails/send-with-attachment").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/components/details/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/v1/components/*/visibility").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/v1/components/*/visibility").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/").permitAll()
                        // --- V2 public box endpoints (dashboard + list) ---
                        .requestMatchers(HttpMethod.GET, "/v2/boxes/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Allow preflight requests
                        .requestMatchers("/v2/**").permitAll() // Liberando acesso a todas portas v2/
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(filterSecurity, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
        configuration.setExposedHeaders(List.of("Authorization"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
