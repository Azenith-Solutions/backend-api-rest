package com.azenithsolutions.backendapirest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Expor o diretório de uploads como recurso estático
        String uploadDir = Paths.get("uploads").toAbsolutePath().toString();
        
        // Mapear o caminho /uploads/** para o diretório físico
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir + "/")
                .setCachePeriod(3600) // cache de 1 hora
                .resourceChain(true);
    }
}
