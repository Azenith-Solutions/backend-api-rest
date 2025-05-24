package com.azenithsolutions.backendapirest.v1.controller.files;

import com.azenithsolutions.backendapirest.v1.dto.shared.ApiResponseDTO;
import com.azenithsolutions.backendapirest.v1.service.files.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/images")
@RequiredArgsConstructor
public class ImageController {
    
    @Autowired
    private final ImageService imageService;
    
    @GetMapping("/info/{filename}")
    public ResponseEntity<ApiResponseDTO<?>> getImageInfo(@PathVariable String filename) {
        try {
            String imageUrl = imageService.getImageUrl(filename);
            
            if (imageUrl == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponseDTO<>(
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.value(),
                        "Not Found",
                        "Image not found",
                        "/v1/images/info/" + filename
                    )
                );
            }
            
            Map<String, String> imageInfo = new HashMap<>();
            imageInfo.put("url", imageUrl);
            imageInfo.put("filename", filename);
            
            return ResponseEntity.ok(
                new ApiResponseDTO<>(
                    LocalDateTime.now(),
                    HttpStatus.OK.value(),
                    "Success",
                    imageInfo,
                    "/v1/images/info/" + filename
                )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ApiResponseDTO<>(
                    LocalDateTime.now(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Internal Server Error",
                    "Erro ao obter informações da imagem: " + e.getMessage(),
                    "/v1/images/info/" + filename
                )
            );
        }
    }
}
