package com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.mappers;

import com.azenithsolutions.backendapirest.v1.model.Box;
import com.azenithsolutions.backendapirest.v2.core.domain.model.component.EletronicComponent;
import com.azenithsolutions.backendapirest.v2.core.domain.model.component.valueobjects.Category;
import com.azenithsolutions.backendapirest.v2.core.domain.model.component.valueobjects.Status;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.entity.EletronicComponentEntity;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.components.EletronicComponentResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class EletronicComponentMapper {
    public static EletronicComponentResponseDTO toResponseDTO(EletronicComponent componente) {
        if (componente == null) {
            return null;
        }

        EletronicComponentResponseDTO dto = new EletronicComponentResponseDTO();
        dto.setId(componente.getId());
        dto.setIdHardwaretech(componente.getIdHardwaretech());
        dto.setNome(componente.getNome());

//        if (componente.getCaixa() != null) {
//            dto.setCaixaNome(componente.getCaixa().getCodigo());
//        }

        if (componente.getCategoria() != null) {
            dto.setCategoria(componente.getCategoria().getNome());
        }

        dto.setPartNumber(componente.getPartNumber());
        dto.setQuantidade(componente.getQuantidade());
        dto.setAnunciado(componente.getAnunciado());
        dto.setCodigoMercadoLivre(componente.getCodigoMercadoLivre());

        if (componente.getStatus() != null) {
            dto.setVerificado(componente.getStatus().getFlagVerificado());
            dto.setCondicao(componente.getStatus().getCondicao());
            dto.setObservacao(componente.getStatus().getObservacao());
        }

        dto.setS3ImagePath(componente.getS3ImagePath());
        dto.setDataUltimaVenda(componente.getDataUltimaVenda());
        dto.setDataCriacao(componente.getDataCriacao());
        dto.setDataUltimaAtualizacao(componente.getDataUltimaAtualizacao());

        return dto;
    }

    public static List<EletronicComponentResponseDTO> toResponseDTOList(List<EletronicComponent> componentes) {
        return componentes.stream()
                .map(EletronicComponentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public EletronicComponentEntity toEntity(EletronicComponent domain) {
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

        if (domain.getCategoria() != null) {
            entity.setCategoriaNome(domain.getCategoria().getNome());
            entity.setCategoriaDescricao(domain.getCategoria().getDescricao());
        }

//        if (domain.getCaixa() != null) {
//            entity.setCaixaCodigo(domain.getCaixa().getCodigo());
//            entity.setCaixaLocalizacao(domain.getCaixa().getLocalizacao());
//            entity.setCaixaCapacidadeMaxima(domain.getCaixa().getCapacidadeMaxima());
//            entity.setCaixaOcupacaoAtual(domain.getCaixa().getOcupacaoAtual());
//        }

        if (domain.getStatus() != null) {
            entity.setStatusFlagVerificado(domain.getStatus().getFlagVerificado());
            entity.setStatusCondicao(domain.getStatus().getCondicao());
            entity.setStatusObservacao(domain.getStatus().getObservacao());
        }

        return entity;
    }


    public EletronicComponent toDomain(EletronicComponentEntity entity) {
        if (entity == null) {
            return null;
        }

        // Recriando objetos de valor
        Category categoria = null;
        if (entity.getCategoriaNome() != null) {
            categoria = Category.criar(
                    entity.getCategoriaNome(),
                    entity.getCategoriaDescricao()
            );
        }

        Box caixa = null;
        if (entity.getCaixaCodigo() != null) {
            caixa = Box.criar(
                    entity.getCaixaCodigo(),
                    entity.getCaixaLocalizacao(),
                    entity.getCaixaCapacidadeMaxima() != null ? entity.getCaixaCapacidadeMaxima() : 100
            );
        }

        Status status = null;
        if (entity.getStatusFlagVerificado() != null) {
            if (entity.getStatusFlagVerificado()) {
                status = Status.verificado(
                        entity.getStatusCondicao(),
                        entity.getStatusObservacao()
                );
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
}
