package com.azenithsolutions.backendapirest.v2.core.domain.model.item;

import com.azenithsolutions.backendapirest.v2.core.domain.model.box.Box;
import com.azenithsolutions.backendapirest.v2.core.domain.model.category.Category;
import com.azenithsolutions.backendapirest.v2.core.domain.model.component.EletronicComponent;
import com.azenithsolutions.backendapirest.v2.core.domain.model.component.valueobjects.Status;
import com.azenithsolutions.backendapirest.v2.core.domain.model.enums.OrderStatus;
import com.azenithsolutions.backendapirest.v2.core.domain.model.order.Order;

import java.time.LocalDateTime;
import java.util.Date;

public class Item {
    private Long idItem;
    private EletronicComponent fkComponente;
    private Order fkPedido;
    private Integer quantidadeCarrinho;

    private Item(Long idItem, EletronicComponent fkComponente, Order fkPedido, Integer quantidadeCarrinho) {
        this.idItem = idItem;
        this.fkComponente = fkComponente;
        this.fkPedido = fkPedido;
        this.quantidadeCarrinho = quantidadeCarrinho;
    }

    public static Item create(Long idItem,
                              Integer quantidadeCarrinho,
                              Long idOrder,
                              String codigo,
                              String nomeComprador,
                              String emailComprador,
                              String cnpj,
                              String valor,
                              OrderStatus status,
                              String telCelular,
                              LocalDateTime createdAt,
                              LocalDateTime updatedAt,
                              Long idComponent,
                              String idHardwaretech,
                              String nome,
                              Box caixa,
                              Category categoria,
                              String partNumber,
                              int quantidade,
                              Boolean anunciado,
                              String codigoMercadoLivre,
                              Status statusComponent,
                              String s3ImagePath,
                              Date dataUltimaVenda,
                              Date dataCriacao,
                              Date dataUltimaAtualizacao ){
        return new Item(
                idItem,
                EletronicComponent.recriar(idComponent, idHardwaretech, nome, caixa, categoria, partNumber, quantidade, anunciado, codigoMercadoLivre, statusComponent, s3ImagePath, dataUltimaVenda, dataCriacao, dataUltimaAtualizacao),
                new Order(idOrder, codigo, nomeComprador, emailComprador, cnpj, valor, status, telCelular, createdAt, updatedAt),
                quantidadeCarrinho
        );
    }

    public Long getIdItem() {
        return idItem;
    }

    public void setIdItem(Long idItem) {
        this.idItem = idItem;
    }

    public EletronicComponent getFkComponente() {
        return fkComponente;
    }

    public void setFkComponente(EletronicComponent fkComponente) {
        this.fkComponente = fkComponente;
    }

    public Order getFkPedido() {
        return fkPedido;
    }

    public void setFkPedido(Order fkPedido) {
        this.fkPedido = fkPedido;
    }

    public Integer getQuantidadeCarrinho() {
        return quantidadeCarrinho;
    }

    public void setQuantidadeCarrinho(Integer quantidadeCarrinho) {
        this.quantidadeCarrinho = quantidadeCarrinho;
    }
}
