package com.azenithsolutions.backendapirest.v2.infrastructure.web.controller;

import com.azenithsolutions.backendapirest.v2.core.domain.model.item.Item;
import com.azenithsolutions.backendapirest.v2.core.usecase.item.CreateItemUseCase;
import com.azenithsolutions.backendapirest.v2.core.usecase.item.GetAllItemUseCase;
import com.azenithsolutions.backendapirest.v2.core.usecase.item.GetItemByIdUseCase;
import com.azenithsolutions.backendapirest.v2.core.usecase.item.UpdateItemUseCase;
import com.azenithsolutions.backendapirest.v2.core.usecase.item.command.ItemCreateCommand;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.item.ItemRequestDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.shared.ApiResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Item Management - v2", description = "Endpoints to manage items")
@RestController("itemControllerV2")
@RequestMapping("/v2/items")
@RequiredArgsConstructor
public class ItemController {

    private final CreateItemUseCase createItem;
    private final GetAllItemUseCase getAllItems;
    private final GetItemByIdUseCase getItemByIdUseCase;
    private final UpdateItemUseCase updateItem;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<?>> getAllItems(HttpServletRequest request) {
        try {
            List<Item> items = getAllItems.execute();

            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.OK.value(),
                                    "OK",
                                    items,
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

    @GetMapping("{id}")
    public ResponseEntity<ApiResponseDTO<?>> getItemById(@PathVariable Long id) {
        try {
            Item itemWithId = getItemByIdUseCase.execute(id);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.OK.value(),
                                    "OK",
                                    itemWithId,
                                    null
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
                                    null
                            )
                    );
        }
    }

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

    @PutMapping("{id}")
    public ResponseEntity<ApiResponseDTO<?>> updateItem(@PathVariable Long id, @Valid @RequestBody
    ItemRequestDTO itemRequestDTO, HttpServletRequest request) {
        try {
            Item item = updateItem.execute(id, itemRequestDTO);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.OK.value(),
                                    "Item atualizado com sucesso!",
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
