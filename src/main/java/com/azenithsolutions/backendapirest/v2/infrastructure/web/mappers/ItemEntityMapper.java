package com.azenithsolutions.backendapirest.v2.infrastructure.web.mappers;

import com.azenithsolutions.backendapirest.v2.core.domain.model.component.valueobjects.Status;
import com.azenithsolutions.backendapirest.v2.core.domain.model.item.Item;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.entity.EletronicComponentEntity;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.entity.ItemEntity;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.entity.OrderEntity;

public class ItemEntityMapper {

    public static Item toDomain(ItemEntity entity) {
        if (entity == null) {
            return null;
        }

        Status status = null;
        if (entity.getFkComponente().getFlagVerificado() != null) {
            if (entity.getFkComponente().getFlagVerificado()) {
                status = Status.verificado(entity.getFkComponente().getCondicao(), entity.getFkComponente().getObservacao());
            } else {
                status = Status.naoVerificado(entity.getFkComponente().getObservacao());
            }
        }

        EletronicComponentEntity eletronicComponentEntity = entity.getFkComponente() != null ? entity.getFkComponente() : null;
        OrderEntity orderEntity = entity.getFkPedido() != null ? entity.getFkPedido() : null;

        return Item.create(
                entity.getIdItem(),
                entity.getQuantidadeCarrinho(),
                orderEntity.getId(),
                orderEntity.getCodigo(),
                orderEntity.getNomeComprador(),
                orderEntity.getEmailComprador(),
                orderEntity.getCnpj(),
                orderEntity.getValor(),
                orderEntity.getStatus(),
                orderEntity.getTelCelular(),
                orderEntity.getCreatedAt(),
                orderEntity.getUpdatedAt(),
                eletronicComponentEntity.getId(),
                eletronicComponentEntity.getIdHardwaretech(),
                eletronicComponentEntity.getNome(),
                null,
                null, // Ajustar campos nulls
                eletronicComponentEntity.getPartNumber(),
                eletronicComponentEntity.getQuantidade(),
                eletronicComponentEntity.getFlagVerificado(),
                eletronicComponentEntity.getObservacao(),
                status,
                eletronicComponentEntity.getS3ImagePath(),
                eletronicComponentEntity.getDataUltimaVenda(),
                eletronicComponentEntity.getDataCriacao(),
                eletronicComponentEntity.getDataUltimaAtualizacao()
        );
    }

    public static ItemEntity toEntity(Item domain) {
        if (domain == null) {
            return null;
        }

        ItemEntity entity = new ItemEntity();
        entity.setIdItem(domain.getIdItem());
        entity.setQuantidadeCarrinho(domain.getQuantidadeCarrinho());

        if (domain.getFkComponente() != null) {
            EletronicComponentEntity componentEntity = new EletronicComponentEntity();
            componentEntity.setId(domain.getFkComponente().getId());
            componentEntity.setNome(domain.getFkComponente().getNome());
            componentEntity.setFkCategoria(domain.getFkComponente().getCategoria() !=  null ? domain.getFkComponente().getCategoria().getId() : null);
            componentEntity.setPartNumber(domain.getFkComponente().getPartNumber());
            componentEntity.setQuantidade(domain.getFkComponente().getQuantidade());
            entity.setFkComponente(componentEntity);
        }

        if (domain.getFkPedido() != null) {
            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setId(domain.getFkPedido().getId());
            orderEntity.setCodigo(domain.getFkPedido().getCodigo());
            orderEntity.setNomeComprador(domain.getFkPedido().getNomeComprador());
            orderEntity.setEmailComprador(domain.getFkPedido().getEmailComprador());
            orderEntity.setCnpj(domain.getFkPedido().getCnpj());
            orderEntity.setValor(domain.getFkPedido().getValor());
            orderEntity.setStatus(domain.getFkPedido().getStatus());
            orderEntity.setTelCelular(domain.getFkPedido().getTelCelular());
            orderEntity.setCreatedAt(domain.getFkPedido().getCreatedAt());
            orderEntity.setUpdatedAt(domain.getFkPedido().getUpdatedAt());
            entity.setFkPedido(orderEntity);
        }

        return entity;
    }
}
