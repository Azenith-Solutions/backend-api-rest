package com.azenithsolutions.backendapirest.v2.infrastructure.web.mappers;

import com.azenithsolutions.backendapirest.v2.core.domain.model.box.Box;
import com.azenithsolutions.backendapirest.v2.core.domain.model.category.Category;
import com.azenithsolutions.backendapirest.v2.core.domain.model.component.EletronicComponent;
import com.azenithsolutions.backendapirest.v2.core.domain.model.component.valueobjects.Status;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.entity.BoxEntity;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.entity.CategoryEntity;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.entity.EletronicComponentEntity;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.components.ComponentCatalogResponseDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.components.ComponentManagerResponseDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.components.EletronicComponentResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class EletronicComponentMapper {
    
    // Conversão de Domain para ResponseDTO
    public static EletronicComponentResponseDTO toResponseDTO(EletronicComponent component) {
        if (component == null) {
            return null;
        }
        
        EletronicComponentResponseDTO dto = new EletronicComponentResponseDTO();
        dto.setId(component.getId());
        dto.setIdHardwaretech(component.getIdHardwaretech());
        dto.setNome(component.getNome());
        
        if (component.getCaixa() != null) {
            dto.setCaixaID(component.getCaixa().getIdCaixa());
        }
        
        dto.setCategoria(component.getCategoria() != null ? component.getCategoria().getNomeCategoria() : null);
        dto.setPartNumber(component.getPartNumber());
        dto.setQuantidade(component.getQuantidade());
        dto.setAnunciado(component.getAnunciado());
        dto.setCodigoMercadoLivre(component.getCodigoMercadoLivre());
        
        if (component.getStatus() != null) {
            dto.setVerificado(component.getStatus().getFlagVerificado());
            dto.setCondicao(component.getStatus().getCondicao());
            dto.setObservacao(component.getStatus().getObservacao());
            dto.setStatus(component.getStatus().toString());
        }
        
        dto.setS3ImagePath(component.getS3ImagePath());
        dto.setDataUltimaVenda(component.getDataUltimaVenda());
        dto.setDataCriacao(component.getCreatedAt());
        dto.setDataUltimaAtualizacao(component.getUpdatedAt());
        
        return dto;
    }
    
    // Conversão de lista de Domain para lista de ResponseDTO
    public static List<EletronicComponentResponseDTO> toResponseDTOList(List<EletronicComponent> components) {
        return components.stream()
            .map(EletronicComponentMapper::toResponseDTO)
            .collect(Collectors.toList());
    }
    
    // Conversão de Domain para Entity (JPA)
    public static EletronicComponentEntity toEntity(EletronicComponent domain) {
        if (domain == null) {
            return null;
        }
        
        EletronicComponentEntity entity = new EletronicComponentEntity();
        entity.setId(domain.getId());
        entity.setIdHardwaretech(domain.getIdHardwaretech());
        entity.setNome(domain.getNome());
        entity.setPartNumber(domain.getPartNumber());
        entity.setQuantidade(domain.getQuantidade());
        entity.setCodigoML(domain.getCodigoMercadoLivre());
        entity.setS3ImagePath(domain.getS3ImagePath());
        entity.setDataUltimaVenda(domain.getDataUltimaVenda());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setIsVisibleCatalog(domain.getAnunciado());
        entity.setFlagML(domain.getAnunciado());
        
        // Mapear Categoria (valueobject) para campos básicos
        if (domain.getCategoria() != null) {
            CategoryEntity categoryEntity = CategoryEntityMapper.toEntity(domain.getCategoria());
            entity.setFkCategoria(categoryEntity);
        }
        
        // Mapear Caixa 
        if (domain.getCaixa() != null) {
            BoxEntity boxEntity = BoxRestMapper.toEntity(domain.getCaixa());
            entity.setFkCaixa(boxEntity);
        }
        
        // Mapear Status (valueobject) para campos básicos
        if (domain.getStatus() != null) {
            entity.setFlagVerificado(domain.getStatus().getFlagVerificado());
            entity.setCondicao(domain.getStatus().getCondicao());
            entity.setObservacao(domain.getStatus().getObservacao());
        }
        
        return entity;
    }
    
    // Conversão de Entity (JPA) para Domain
//    public static EletronicComponent toDomain(EletronicComponentEntity entity) {
//        return toDomain(entity, null, null);
//    }
//
    // Conversão de Entity (JPA) para Domain com dependências resolvidas
    public static EletronicComponent toDomain(EletronicComponentEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Status status = null;
        if (entity.getFlagVerificado() != null) {
            if (entity.getFlagVerificado()) {
                status = Status.verificado(entity.getCondicao(), entity.getObservacao());
            } else {
                status = Status.naoVerificado(entity.getObservacao());
            }
        }

        Box caixa = BoxRestMapper.toDomain(entity.getFkCaixa());
        Category categoria = CategoryEntityMapper.toDomain(entity.getFkCategoria());

        
        return EletronicComponent.recriar(
            entity.getId(),
            entity.getIdHardwaretech(),
            entity.getNome(),
            caixa,
            categoria,
            entity.getPartNumber(),
            entity.getQuantidade(),
            entity.getIsVisibleCatalog(), // Usando isVisibleCatalog como anunciado
            entity.getCodigoML(),
            status,
            entity.getS3ImagePath(),
            entity.getDataUltimaVenda(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
    
    // Conversão de lista de Entity para lista de Domain
    public static List<EletronicComponent> toDomainList(List<EletronicComponentEntity> entities) {
        return entities.stream()
            .map(EletronicComponentMapper::toDomain)
            .collect(Collectors.toList());
    }

    // Conversão de lista de Entity para lista de Domain
    public static List<ComponentCatalogResponseDTO> tonListCatalogDTO(List<EletronicComponentEntity> entities) {
        return entities.stream()
                .map(EletronicComponentMapper::toResponseCatalogDTO)
                .collect(Collectors.toList());
    }

    public static ComponentCatalogResponseDTO toResponseCatalogDTO(EletronicComponentEntity entity){
        if (entity == null) {
            return null;
        }

        Status status = null;
        if (entity.getFlagVerificado() != null) {
            if (entity.getFlagVerificado()) {
                status = Status.verificado(entity.getCondicao(), entity.getObservacao());
            } else {
                status = Status.naoVerificado(entity.getObservacao());
            }
        }

        return new ComponentCatalogResponseDTO(
                entity.getId(),
                entity.getNome(),
                entity.getFkCategoria(),
                entity.getQuantidade(),
                entity.getDescricao(),
                entity.getS3ImagePath()
        );
    }

    public static List<ComponentManagerResponseDTO> tonListManagerDTO(List<EletronicComponentEntity> entities) {
        return entities.stream()
                .map(EletronicComponentMapper::toResponseManagerDTO)
                .collect(Collectors.toList());
    }

    public static ComponentManagerResponseDTO toResponseManagerDTO(EletronicComponentEntity entity){
        if (entity == null) {
            return null;
        }

        Status status = null;
        if (entity.getFlagVerificado() != null) {
            if (entity.getFlagVerificado()) {
                status = Status.verificado(entity.getCondicao(), entity.getObservacao());
            } else {
                status = Status.naoVerificado(entity.getObservacao());
            }
        }

        return new ComponentManagerResponseDTO(
                entity.getId(),
                entity.getIdHardwaretech(),
            entity.getIdQrcode(),
                entity.getNome(),
                entity.getFkCaixa(),
                entity.getFkCategoria(),
                entity.getPartNumber(),
                entity.getQuantidade(),
                entity.getFlagML(),
                entity.getCodigoML(),
                entity.getFlagVerificado(),
                entity.getCondicao(),
                entity.getObservacao(),
                entity.getDescricao(),
                entity.getS3ImagePath(),
                entity.getDataUltimaVenda(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getQuantidadeVendido(),
                entity.getIsVisibleCatalog()
        );
    }


}
