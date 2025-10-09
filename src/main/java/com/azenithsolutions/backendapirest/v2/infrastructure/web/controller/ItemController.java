package com.azenithsolutions.backendapirest.v2.infrastructure.web.controller;

import com.azenithsolutions.backendapirest.v2.core.domain.model.item.Item;
import com.azenithsolutions.backendapirest.v2.core.usecase.item.CreateItemUseCase;
import com.azenithsolutions.backendapirest.v2.core.usecase.item.command.ItemCreateCommand;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.item.ItemRequestDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.shared.ApiResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Item Management - v2", description = "Endpoints to manage items")
@RestController
@RequestMapping("/v2/items")
@RequiredArgsConstructor
public class ItemController {

    private CreateItemUseCase createItem;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<?>> createItem(@RequestBody List<ItemRequestDTO> itemRequestDTOS, HttpServletRequest request) {
        System.out.println("Estou entrando no createItem");

        try {
            System.out.println("Estou entrando no try do createItem");
            List<ItemCreateCommand> itemMapper = itemRequestDTOS.stream().map(i -> new ItemCreateCommand(i.getFkComponente(), i.getFkPedido(), i.getQuantidadeCarrinho())).toList();

            List<Item> item = createItem.execute(itemMapper);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.CREATED.value(),
                                    "Item criado com sucesso!",
                                    item,
                                    request.getRequestURI()
                            )
                    );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                    "Erro interno: " + e.getMessage(),
                                    null,
                                    request.getRequestURI()
                            )
                    );
        }
    }
}
