package com.azenithsolutions.backendapirest.v2.core.usecase.components;

import com.azenithsolutions.backendapirest.v2.core.domain.model.box.Box;
import com.azenithsolutions.backendapirest.v2.core.domain.model.category.Category;
import com.azenithsolutions.backendapirest.v2.core.domain.model.component.EletronicComponent;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.BoxGateway;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.CategoryRepositoryGateway;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.EletronicComponentGateway;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.ImageStorageGateway;
import com.azenithsolutions.backendapirest.v2.core.usecase.components.command.CreateComponentWithFileCommand;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.components.ComponentRequestDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class CreateComponentWithFileUseCase {
    private final EletronicComponentGateway gateway;
    private final ImageStorageGateway imageStorageGateway;
    private final CategoryRepositoryGateway categoryRepositoryGateway;
    private final BoxGateway boxGateway;


    public CreateComponentWithFileUseCase(EletronicComponentGateway gateway, ImageStorageGateway imageStorageGateway, CategoryRepositoryGateway categoryRepositoryGateway, BoxGateway boxGateway) {
        this.gateway = gateway;
        this.imageStorageGateway = imageStorageGateway;
        this.categoryRepositoryGateway = categoryRepositoryGateway;
        this.boxGateway = boxGateway;
    }

    public EletronicComponent execute(ComponentRequestDTO command, MultipartFile file) {
        List<Category> categorias = categoryRepositoryGateway.findAll();
        Category categoria = categorias.stream().filter(item -> item.getIdCategoria().equals(command.getFkCategoria())).findFirst().orElseThrow();

        List<Box> boxes = boxGateway.findAll();
        Box box = boxes.stream().filter(item -> item.getIdCaixa().equals(command.getFkCaixa())).findFirst().orElseThrow();


        EletronicComponent componente = EletronicComponent.criarNovo(
                command.getNomeComponente(),
                categoria,
                command.getPartNumber(),
                command.getQuantidade(),
                box
        );

        // 4. Salvar imagem, se houver
        if (file != null && !file.isEmpty()) {
            String storedFileName = imageStorageGateway.saveImage(file);
            componente.setS3ImagePath(storedFileName);
        }

        return gateway.save(componente);
    }
}
