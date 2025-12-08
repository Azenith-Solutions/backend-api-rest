package com.azenithsolutions.backendapirest.v2.core.usecase.item;

import com.azenithsolutions.backendapirest.v2.core.domain.model.component.EletronicComponent;
import com.azenithsolutions.backendapirest.v2.core.domain.model.item.Item;
import com.azenithsolutions.backendapirest.v2.core.domain.model.order.Order;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.EletronicComponentGateway;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.ItemGateway;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.OrderGateway;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.item.ItemRequestDTO;
import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;

public class UpdateItemUseCase {
    private ItemGateway itemGateway;
    private EletronicComponentGateway eletronicComponentGateway;
    private OrderGateway orderGateway;

    public UpdateItemUseCase(ItemGateway itemGateway, EletronicComponentGateway eletronicComponentGateway, OrderGateway orderGateway) {
        this.itemGateway = itemGateway;
        this.eletronicComponentGateway = eletronicComponentGateway;
        this.orderGateway = orderGateway;
    }

    public Item execute(Long id, ItemRequestDTO itemRequestDTO){
        Item item = itemGateway.findById(id);
        if(item.equals(null)){
            throw new EntityNotFoundException("Usuário não encontrado");
        }

        if (itemRequestDTO.getFkComponente() == null) {
            throw new IllegalArgumentException("ID do componente não pode ser nulo");
        }
        if (itemRequestDTO.getFkPedido() == null) {
            throw new IllegalArgumentException("ID do pedido não pode ser nulo");
        }
        if (itemRequestDTO.getQuantidadeCarrinho() == null || itemRequestDTO.getQuantidadeCarrinho() <= 0) {
            throw new IllegalArgumentException("QuantidadeCarrinho deve ser maior que zero");
        }


        Optional<EletronicComponent> eletronicComponent = eletronicComponentGateway.findById(itemRequestDTO.getFkComponente());

        Order order = orderGateway.findById(itemRequestDTO.getFkPedido());

        item.setFkComponente(eletronicComponent.get());
        item.setFkPedido(order);
        item.setQuantidadeCarrinho(itemRequestDTO.getQuantidadeCarrinho());

        return itemGateway.save(item);
    }
}
