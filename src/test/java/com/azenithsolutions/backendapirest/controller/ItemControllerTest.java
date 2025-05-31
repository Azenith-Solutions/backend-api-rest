package com.azenithsolutions.backendapirest.controller;

import com.azenithsolutions.backendapirest.v1.controller.item.ItemController;
import com.azenithsolutions.backendapirest.v1.dto.item.ItemRequestDTO;
import com.azenithsolutions.backendapirest.v1.dto.shared.ApiResponseDTO;
import com.azenithsolutions.backendapirest.v1.model.Item;

import com.azenithsolutions.backendapirest.v1.service.item.ItemService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import java.util.List;
import java.util.Optional;

public class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve retornar sucesso 200 ao listar os items")
    void testGetItem_sucess(){
        Item item1 = new Item();
        item1.setIdItem(1l);
        item1.setQuantidadeCarrinho(2);

        Item item2 = new Item();
        item2.setIdItem(2l);
        item2.setQuantidadeCarrinho(5);

        when(itemService.getAllItems()).thenReturn(List.of(item1, item2));
        when(request.getRequestURI()).thenReturn("/items");

        ResponseEntity<ApiResponseDTO<?>> response = itemController.getAllItems(request);

        assertEquals(200, response.getStatusCodeValue());

    }

    @Test
    @DisplayName("Deve retornar erro 500 ao listar os items")
    void testGetItem_error(){
        when(itemService.getAllItems()).thenThrow(new RuntimeException("Internal Error"));
        when(request.getRequestURI()).thenReturn("/items");

        ResponseEntity<ApiResponseDTO<?>> response = itemController.getAllItems(request);

        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    @DisplayName("Deve retornar sucesso 200 ao retornar um item")
    void testGetItemId_sucess(){
        Item item = new Item();
        item.setIdItem(1L);
        item.setQuantidadeCarrinho(2);

        when(itemService.getItemById(1L)).thenReturn(Optional.of(item));
        when(request.getRequestURI()).thenReturn("/items/1L");

        ResponseEntity<ApiResponseDTO<?>> response = itemController.getItemById(1L);
        ApiResponseDTO<?> body = response.getBody();
        Item itemBody = (Item) body.getData();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, itemBody.getQuantidadeCarrinho());
    }

    @Test
    @DisplayName("Deve retornar erro 500 ao retornar um item")
    void testGetItemId_error(){
        when(itemService.getItemById(1L)).thenThrow(new IllegalArgumentException());
        when(request.getRequestURI()).thenReturn("/items/1L");

        ResponseEntity<ApiResponseDTO<?>> response = itemController.getItemById(1L);

        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    @DisplayName("Deve retornar sucesso 201 ao criar um item")
    void testCreateItem_success() {
        Item itemCriado = new Item();
        itemCriado.setIdItem(1L);
        itemCriado.setQuantidadeCarrinho(3);

        Item itemCriado2 = new Item();
        itemCriado2.setIdItem(2L);
        itemCriado2.setQuantidadeCarrinho(4);

        List<Item> listaCriada = List.of(itemCriado, itemCriado2);

        ItemRequestDTO dto1 = new ItemRequestDTO();
        dto1.setFkComponente(1L);
        dto1.setFkPedido(1L);
        dto1.setQuantidadeCarrinho(3);

        ItemRequestDTO dto2 = new ItemRequestDTO();
        dto2.setFkComponente(2L);
        dto2.setFkPedido(1L);
        dto2.setQuantidadeCarrinho(4);

        when(itemService.createItem(List.of(dto1, dto2))).thenReturn(listaCriada);
        when(request.getRequestURI()).thenReturn("/v1/items");

        ResponseEntity<ApiResponseDTO<?>> response = itemController.createItem(List.of(dto1, dto2), request);

        assertEquals(201, response.getStatusCodeValue());
        ApiResponseDTO<?> body = response.getBody();
        assertNotNull(body);
        assertEquals("Item criado com sucesso!", body.getMessage());
        assertNotNull(body.getData());
        List<?> itens = (List<?>) body.getData();
        assertEquals(2, itens.size());
    }

    @Test
    @DisplayName("Deve retornar erro 500 ao tentar criar um item com dados inválidos")
    void testCreateItem_error() {
        ItemRequestDTO dto = new ItemRequestDTO();
        dto.setFkComponente(null); // dado inválido para simular erro
        dto.setFkPedido(1L);
        dto.setQuantidadeCarrinho(1);

        when(itemService.createItem(List.of(dto))).thenThrow(new IllegalArgumentException("ID do componente não pode ser nulo"));
        when(request.getRequestURI()).thenReturn("/v1/items");

        ResponseEntity<ApiResponseDTO<?>> response = itemController.createItem(List.of(dto), request);

        assertEquals(500, response.getStatusCodeValue());
        ApiResponseDTO<?> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.getMessage().contains("Erro interno"));
        assertNull(body.getData());
    }

    @Test
    @DisplayName("Deve retornar sucesso 200 ao atualizar um item")
    void testuUpdateItem_success() {
        ItemRequestDTO dto = new ItemRequestDTO();
        dto.setFkComponente(1L);
        dto.setFkPedido(1L);
        dto.setQuantidadeCarrinho(5);

        Item itemAtualizado = new Item();
        itemAtualizado.setIdItem(1L);
        itemAtualizado.setQuantidadeCarrinho(5);


        when(itemService.updateItem(1L, dto)).thenReturn(itemAtualizado);
        when(request.getRequestURI()).thenReturn("/v1/items");

        ResponseEntity<ApiResponseDTO<?>> response = itemController.updateItem(1L, dto, request);

        assertEquals(200, response.getStatusCodeValue());
        ApiResponseDTO<?> body = response.getBody();
        assertNotNull(body);
        assertEquals("Item atualizado com sucesso!", body.getMessage());
        assertNotNull(body.getData());
        Item itemBody = (Item) body.getData();
        assertEquals(5, itemBody.getQuantidadeCarrinho());
    }

    @Test
    @DisplayName("Deve retornar erro 404 ao atualizar um item")
    void testUpdateItem_error() {
        Long itemId = 1L;

        ItemRequestDTO itemRequestDTO = new ItemRequestDTO();
        itemRequestDTO.setFkComponente(1L);
        itemRequestDTO.setFkPedido(1L);
        itemRequestDTO.setQuantidadeCarrinho(10);

        when(itemService.updateItem(itemId, itemRequestDTO)).thenThrow(new RuntimeException("Erro interno"));
        when(request.getRequestURI()).thenReturn("/items/" + itemId);

        ResponseEntity<ApiResponseDTO<?>> response = itemController.updateItem(itemId, itemRequestDTO, request);

        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    @DisplayName("Deve retornar erro 204 ao deletar um item")
    void testeDeleteItem_success() {
        Long itemId = 1L;

        doNothing().when(itemService).deleteItem(itemId);

        ResponseEntity<ApiResponseDTO<?>> response = itemController.deleteItem(itemId);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Item não encontrado!", response.getBody().getMessage());
    }

}
