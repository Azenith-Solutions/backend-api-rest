package com.azenithsolutions.backendapirest.v2.core.usecase.components;

import com.azenithsolutions.backendapirest.v2.core.domain.model.box.Box;
import com.azenithsolutions.backendapirest.v2.core.domain.model.category.Category;
import com.azenithsolutions.backendapirest.v2.core.domain.model.component.EletronicComponent;
import com.azenithsolutions.backendapirest.v2.core.domain.model.component.valueobjects.Status;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.BoxGateway;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.CategoryRepositoryGateway;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.EletronicComponentGateway;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.ImageStorageGateway;
import com.azenithsolutions.backendapirest.v2.core.usecase.components.command.UpdateComponentWithFileCommand;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.components.ComponentRequestDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public class UpdateComponentWithFileUseCase {
    private final EletronicComponentGateway gateway;
    private final ImageStorageGateway imageStorageGateway;
    private final CategoryRepositoryGateway categoryRepositoryGateway;
    private final BoxGateway boxGateway;

    public UpdateComponentWithFileUseCase(EletronicComponentGateway gateway, ImageStorageGateway imageStorageGateway, CategoryRepositoryGateway categoryRepositoryGateway, BoxGateway boxGateway) {
        this.gateway = gateway;
        this.imageStorageGateway = imageStorageGateway;
        this.categoryRepositoryGateway = categoryRepositoryGateway;
        this.boxGateway = boxGateway;
    }

    public EletronicComponent execute(Long id, ComponentRequestDTO command, MultipartFile file) {
        Optional<EletronicComponent> componentOpt = gateway.findById(id);
        
        if (componentOpt.isEmpty()) {
            throw new RuntimeException("Componente com ID %d não encontrado".formatted(id));
        }
        
        EletronicComponent component = componentOpt.get();

        List<Category> categorias = categoryRepositoryGateway.findAll();
        Category categoria = categorias.stream().filter(item -> item.getIdCategoria().equals(command.getFkCategoria())).findFirst().orElseThrow();

        List<Box> boxes = boxGateway.findAll();
        Box box = boxes.stream().filter(item -> item.getIdCaixa().equals(command.getFkCaixa())).findFirst().orElseThrow();

        Status status = null;
        if (command.getFlagVerificado() != null) {
            if (command.getFlagVerificado()) {
                status = Status.verificado(command.getCondicao().name(), command.getObservacao());
            } else {
                status = Status.naoVerificado(command.getObservacao());
            }
        }

        component = component.update(
                command.getIdHardWareTech(),
                command.getNomeComponente(),
                box,
                categoria,
                command.getPartNumber(),
                command.getQuantidade(),
                command.getIsVisibleCatalog(),
                command.getCodigoML(),
                status
                );
        
        // Update image path if provided
        // 4. Salvar imagem, se houver
        if (file != null && !file.isEmpty()) {
            String storedFileName = imageStorageGateway.saveImage(file);
            component.setS3ImagePath(storedFileName);
        }
        
        return gateway.save(component);
    }
}
