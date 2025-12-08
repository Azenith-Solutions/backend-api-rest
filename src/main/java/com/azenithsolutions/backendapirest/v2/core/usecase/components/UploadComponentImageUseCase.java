package com.azenithsolutions.backendapirest.v2.core.usecase.components;

import com.azenithsolutions.backendapirest.v2.core.domain.model.component.EletronicComponent;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.EletronicComponentGateway;
import com.azenithsolutions.backendapirest.v2.core.usecase.components.command.UploadComponentImageCommand;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public class UploadComponentImageUseCase {
    private final EletronicComponentGateway gateway;

    public UploadComponentImageUseCase(EletronicComponentGateway gateway) {
        this.gateway = gateway;
    }

    public EletronicComponent execute(Long id, UploadComponentImageCommand command, String imagePath) {
        Optional<EletronicComponent> componentOpt = gateway.findById(id);
        
        if (componentOpt.isEmpty()) {
            throw new RuntimeException("Componente com ID %d não encontrado".formatted(id));
        }
        
        return gateway.updateImagePath(id, imagePath);
    }
}
