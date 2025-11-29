package com.azenithsolutions.backendapirest.v2.infrastructure.config.bean;

import com.azenithsolutions.backendapirest.v2.core.domain.repository.BoxGateway;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.CategoryRepositoryGateway;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.EletronicComponentGateway;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.ImageStorageGateway;
import com.azenithsolutions.backendapirest.v2.core.usecase.components.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EletronicComponentBeanConfig {
    @Bean
    CreateEletronicComponentUseCase createEletronicComponentUseCase(EletronicComponentGateway gateway, CategoryRepositoryGateway categoryRepositoryGateway, BoxGateway boxGateway) { return new CreateEletronicComponentUseCase(gateway, categoryRepositoryGateway,boxGateway); }

    @Bean
    UpdateEletronicComponentUseCase updateEletronicComponentUseCase(EletronicComponentGateway gateway) { return new UpdateEletronicComponentUseCase(gateway); }

    @Bean
    GetEletronicComponentByIdUseCase getEletronicComponentByIdUseCase(EletronicComponentGateway gateway) { return new GetEletronicComponentByIdUseCase(gateway); }

    @Bean
    GetEletronicComponentUseCase getEletronicComponentUseCase(EletronicComponentGateway gateway) { return new GetEletronicComponentUseCase(gateway); }

    @Bean
    DeleteEletronicComponentUseCase deleteEletronicComponentUseCase(EletronicComponentGateway gateway) { return new DeleteEletronicComponentUseCase(gateway); }
    
    @Bean
    GetComponentCatalogUseCase getComponentCatalogUseCase(EletronicComponentGateway gateway) { return new GetComponentCatalogUseCase(gateway); }

    @Bean
    GetFilterComponentsUseCase getFilterComponentsUseCase(EletronicComponentGateway gateway) { return new GetFilterComponentsUseCase(gateway); }
    
    @Bean
    GetComponentDetailsUseCase getComponentDetailsUseCase(EletronicComponentGateway gateway) { return new GetComponentDetailsUseCase(gateway); }
    
    @Bean
    GetLowStockComponentsUseCase getLowStockComponentsUseCase(EletronicComponentGateway gateway) { return new GetLowStockComponentsUseCase(gateway); }
    
    @Bean
    GetComponentsInObservationUseCase getComponentsInObservationUseCase(EletronicComponentGateway gateway) { return new GetComponentsInObservationUseCase(gateway); }
    
    @Bean
    GetIncompleteComponentsUseCase getIncompleteComponentsUseCase(EletronicComponentGateway gateway) { return new GetIncompleteComponentsUseCase(gateway); }
    
    @Bean
    GetComponentsOutOfLastSaleSLAUseCase getComponentsOutOfLastSaleSLAUseCase(EletronicComponentGateway gateway) { return new GetComponentsOutOfLastSaleSLAUseCase(gateway); }
    
    @Bean
    GetCountOfTrueAndFalseFlagMLUseCase getCountOfTrueAndFalseFlagMLUseCase(EletronicComponentGateway gateway) { return new GetCountOfTrueAndFalseFlagMLUseCase(gateway); }
    
    @Bean
    UpdateComponentVisibilityUseCase updateComponentVisibilityUseCase(EletronicComponentGateway gateway) { return new UpdateComponentVisibilityUseCase(gateway); }
    
    @Bean
    UploadComponentImageUseCase uploadComponentImageUseCase(EletronicComponentGateway gateway) { return new UploadComponentImageUseCase(gateway); }
    
    @Bean
    CreateComponentWithFileUseCase createComponentWithFileUseCase(EletronicComponentGateway gateway, ImageStorageGateway imageStorageGateway, CategoryRepositoryGateway categoryRepositoryGateway, BoxGateway boxGateway) { return new CreateComponentWithFileUseCase(gateway, imageStorageGateway, categoryRepositoryGateway, boxGateway); }
    
    @Bean
    UpdateComponentWithFileUseCase updateComponentWithFileUseCase(EletronicComponentGateway gateway) { return new UpdateComponentWithFileUseCase(gateway); }
}
