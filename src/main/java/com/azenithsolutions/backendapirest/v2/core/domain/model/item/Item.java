package com.azenithsolutions.backendapirest.v2.core.domain.model.item;

import com.azenithsolutions.backendapirest.v2.core.domain.model.component.EletronicComponent;
import com.azenithsolutions.backendapirest.v2.core.domain.model.order.Order;

public class Item {
    private Long idItem;
    private EletronicComponent fkComponente;
    private Order fkPedido;
    private Integer quantidadeCarrinho;
}
