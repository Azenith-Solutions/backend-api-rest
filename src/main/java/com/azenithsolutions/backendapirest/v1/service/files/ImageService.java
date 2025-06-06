package com.azenithsolutions.backendapirest.v1.service.files;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ImageService {
    private final Path imageFolder = Paths.get("uploads/images");
    private final String imageUrlPrefix = "/uploads/images/";

    public ImageService() {
        try {
            Files.createDirectories(imageFolder);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao criar pasta para salvar imagens", e);
        }
    }    
    
    public String saveImage(MultipartFile file) {
        try {
            String originalName = StringUtils.cleanPath(file.getOriginalFilename() != null ? file.getOriginalFilename() : "unnamed-file");
            Path path = imageFolder.resolve(originalName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            // Retorna a URL relativa para acessar a imagem
            return imageUrlPrefix + originalName;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar a imagem", e);
        }
    }
    
    // Método para construir a URL de uma imagem existente
    public String getImageUrl(String imageName) {
        if (imageName == null || imageName.isEmpty()) {
            return null;
        }
        return imageUrlPrefix + imageName;
    }
    
    // Método para verificar se uma imagem existe
    public boolean imageExists(String imageName) {
        if (imageName == null || imageName.isEmpty()) {
            return false;
        }
        Path imagePath = imageFolder.resolve(imageName);
        return Files.exists(imagePath);
    }
    
    // Método para deletar uma imagem
    public boolean deleteImage(String imageName) {
        if (imageName == null || imageName.isEmpty()) {
            return false;
        }
        try {
            Path imagePath = imageFolder.resolve(imageName);
            if (Files.exists(imagePath)) {
                Files.delete(imagePath);
                return true;
            }
            return false;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao deletar a imagem", e);
        }
    }
}
