package com.azenithsolutions.backendapirest.v2.infrastructure.web.mappers;

import com.azenithsolutions.backendapirest.v2.core.usecase.item.command.ItemCreateCommand;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.item.ItemRequestDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemRestMapper {
    public static List<ItemCreateCommand> toCommands(List<ItemRequestDTO> items) {
        if (items == null) {
            return new ArrayList<>();
        }
        
        return items.stream()
                .map(item -> new ItemCreateCommand(
                        item.getFkComponente(),
                        item.getFkPedido(),
                        item.getQuantidadeCarrinho()
                ))
                .collect(Collectors.toList());
    }
    
    public static List<ItemRequestDTO> toDTOs(List<ItemCreateCommand> commands) {
        if (commands == null) {
            return new ArrayList<>();
        }
        
        return commands.stream()
                .map(command -> new ItemRequestDTO(
                        command.fkComponente(),
                        command.fkPedido(),
                        command.quantidadeCarrinho()
                ))
                .collect(Collectors.toList());
    }
}
