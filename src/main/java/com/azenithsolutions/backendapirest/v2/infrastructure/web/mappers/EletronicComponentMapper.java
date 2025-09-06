package com.azenithsolutions.backendapirest.v2.infrastructure.web.mappers;

import com.azenithsolutions.backendapirest.v2.core.domain.model.box.Box;
import com.azenithsolutions.backendapirest.v2.core.domain.model.component.EletronicComponent;
import com.azenithsolutions.backendapirest.v2.core.domain.model.component.valueobjects.Category;
import com.azenithsolutions.backendapirest.v2.core.domain.model.component.valueobjects.Status;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.entity.EletronicComponentEntity;
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
            dto.setCaixaNome(component.getCaixa().getCodigo());
        }
        
        dto.setCategoria(component.getCategoria() != null ? component.getCategoria().getNome() : null);
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
        dto.setDataCriacao(component.getDataCriacao());
        dto.setDataUltimaAtualizacao(component.getDataUltimaAtualizacao());
        
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
        entity.setAnunciado(domain.getAnunciado());
        entity.setCodigoMercadoLivre(domain.getCodigoMercadoLivre());
        entity.setS3ImagePath(domain.getS3ImagePath());
        entity.setDataUltimaVenda(domain.getDataUltimaVenda());
        entity.setDataCriacao(domain.getDataCriacao());
        entity.setDataUltimaAtualizacao(domain.getDataUltimaAtualizacao());
        
        // Mapear Categoria (valueobject)
        if (domain.getCategoria() != null) {
            entity.setCategoriaNome(domain.getCategoria().getNome());
            entity.setCategoriaDescricao(domain.getCategoria().getDescricao());
        }
        
        // Mapear Status (valueobject)
        if (domain.getStatus() != null) {
            entity.setStatusFlagVerificado(domain.getStatus().getFlagVerificado());
            entity.setStatusCondicao(domain.getStatus().getCondicao());
            entity.setStatusObservacao(domain.getStatus().getObservacao());
        }
        
        // Mapear Caixa (entidade relacionada)
        if (domain.getCaixa() != null) {
            entity.setCaixaCodigo(domain.getCaixa().getCodigo());
            entity.setCaixaLocalizacao(domain.getCaixa().getLocalizacao());
            entity.setCaixaCapacidadeMaxima(domain.getCaixa().getCapacidadeMaxima());
            entity.setCaixaOcupacaoAtual(domain.getCaixa().getOcupacaoAtual());
        }
        
        return entity;
    }
    
    // Conversão de Entity (JPA) para Domain
    public static EletronicComponent toDomain(EletronicComponentEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Box caixa = null;
        if (entity.getCaixaCodigo() != null) {
            caixa = Box.recriar(
                null, // id da caixa pode ser null neste contexto
                entity.getCaixaCodigo(),
                entity.getCaixaLocalizacao(),
                entity.getCaixaCapacidadeMaxima() != null ? entity.getCaixaCapacidadeMaxima() : 0,
                entity.getCaixaOcupacaoAtual() != null ? entity.getCaixaOcupacaoAtual() : 0
            );
        }
        
        Category categoria = null;
        if (entity.getCategoriaNome() != null) {
            categoria = Category.criar(entity.getCategoriaNome(), entity.getCategoriaDescricao());
        }
        
        Status status = null;
        if (entity.getStatusFlagVerificado() != null) {
            if (entity.getStatusFlagVerificado()) {
                status = Status.verificado(entity.getStatusCondicao(), entity.getStatusObservacao());
            } else {
                status = Status.naoVerificado(entity.getStatusObservacao());
            }
        }
        
        return EletronicComponent.recriar(
            entity.getId(),
            entity.getIdHardwaretech(),
            entity.getNome(),
            caixa,
            categoria,
            entity.getPartNumber(),
            entity.getQuantidade(),
            entity.getAnunciado(),
            entity.getCodigoMercadoLivre(),
            status,
            entity.getS3ImagePath(),
            entity.getDataUltimaVenda(),
            entity.getDataCriacao(),
            entity.getDataUltimaAtualizacao()
        );
    }
    
    // Conversão de lista de Entity para lista de Domain
    public static List<EletronicComponent> toDomainList(List<EletronicComponentEntity> entities) {
        return entities.stream()
            .map(EletronicComponentMapper::toDomain)
            .collect(Collectors.toList());
    }
}
