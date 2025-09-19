package com.azenithsolutions.backendapirest.v2.infrastructure.storage.adapter;

import com.azenithsolutions.backendapirest.v2.core.domain.repository.ImageStorageGateway;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ImageStorageGatewayAdapter implements ImageStorageGateway {
    private final Path imageFolder = Paths.get("uploads/images");
    private final String imageUrlPrefix = "/uploads/images/";

    public ImageStorageGatewayAdapter() {
        try {
            Files.createDirectories(imageFolder);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao criar pasta para salvar imagens", e);
        }
    }

    @Override
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

    @Override
    public String getImageUrl(String imageName) {
        if (imageName == null || imageName.isEmpty()) {
            return null;
        }
        return imageUrlPrefix + imageName;
    }

    @Override
    public boolean imageExists(String imageName) {
        if (imageName == null || imageName.isEmpty()) {
            return false;
        }
        Path imagePath = imageFolder.resolve(imageName);
        return Files.exists(imagePath);
    }

    @Override
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
