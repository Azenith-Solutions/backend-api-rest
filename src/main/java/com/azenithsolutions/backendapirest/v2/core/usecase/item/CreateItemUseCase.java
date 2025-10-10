package com.azenithsolutions.backendapirest.v2.core.usecase.item;

import com.azenithsolutions.backendapirest.v2.core.domain.model.component.EletronicComponent;
import com.azenithsolutions.backendapirest.v2.core.domain.model.item.Item;
import com.azenithsolutions.backendapirest.v2.core.domain.model.order.Order;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.EletronicComponentGateway;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.ItemGateway;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.OrderGateway;
import com.azenithsolutions.backendapirest.v2.core.usecase.item.command.ItemCreateCommand;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.entity.EletronicComponentEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CreateItemUseCase {
    private ItemGateway itemGateway;
    private EletronicComponentGateway eletronicComponentGateway;
    private OrderGateway orderGateway;

    public CreateItemUseCase(ItemGateway itemGateway, EletronicComponentGateway eletronicComponentGateway, OrderGateway orderGateway) {
        this.itemGateway = itemGateway;
        this.eletronicComponentGateway = eletronicComponentGateway;
        this.orderGateway = orderGateway;
    }

    public List<Item> execute(List<ItemCreateCommand> itemCreateCommand){
        List<Item> saveItems = new ArrayList<>();

        itemCreateCommand.forEach(dto -> {
            if (dto.fkComponente() == null) {
                throw new IllegalArgumentException("ID do componente não pode ser nulo");
            }
            if (dto.fkPedido() == null) {
                throw new IllegalArgumentException("ID do pedido não pode ser nulo");
            }
            if (dto.quantidadeCarrinho() == null || dto.quantidadeCarrinho() <= 0) {
                throw new IllegalArgumentException("QuantidadeCarrinho deve ser maior que zero");
            }

            Optional<EletronicComponent> eletronicComponent = eletronicComponentGateway.findById(dto.fkComponente());

            Optional<Order> order = orderGateway.findById(dto.fkPedido());

            Item item = Item.create(
                    null,
                    dto.quantidadeCarrinho(),
                    order.get().getId(),
                    order.get().getCodigo(),
                    order.get().getNomeComprador(),
                    order.get().getEmailComprador(),
                    order.get().getCnpj(),
                    order.get().getValor(),
                    order.get().getStatus(),
                    order.get().getTelCelular(),
                    order.get().getCreatedAt(),
                    order.get().getUpdatedAt(),
                    eletronicComponent.get().getId(),
                    eletronicComponent.get().getIdHardwaretech(),
                    eletronicComponent.get().getNome(),
                    eletronicComponent.get().getCaixa(),
                    eletronicComponent.get().getCategoria(),
                    eletronicComponent.get().getPartNumber(),
                    eletronicComponent.get().getQuantidade(),
                    eletronicComponent.get().getAnunciado(),
                    eletronicComponent.get().getCodigoMercadoLivre(),
                    eletronicComponent.get().getStatus(),
                    eletronicComponent.get().getS3ImagePath(),
                    eletronicComponent.get().getDataUltimaVenda(),
                    eletronicComponent.get().getDataCriacao(),
                    eletronicComponent.get().getDataUltimaAtualizacao()
            );

            saveItems.add(item);

        });

        return itemGateway.saveAll(saveItems);

    }
}
