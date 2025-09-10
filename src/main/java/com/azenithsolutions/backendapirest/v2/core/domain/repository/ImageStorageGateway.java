package com.azenithsolutions.backendapirest.v2.core.domain.repository;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageGateway {
    String saveImage(MultipartFile file);
    String getImageUrl(String imageName);
    boolean imageExists(String imageName);
    boolean deleteImage(String imageName);
}
