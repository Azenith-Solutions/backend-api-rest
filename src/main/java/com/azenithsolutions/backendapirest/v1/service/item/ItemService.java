package com.azenithsolutions.backendapirest.v1.service.item;

import com.azenithsolutions.backendapirest.v1.dto.item.ItemRequestDTO;
import com.azenithsolutions.backendapirest.v1.model.Component;
import com.azenithsolutions.backendapirest.v1.model.Item;
import com.azenithsolutions.backendapirest.v1.model.Order;
import com.azenithsolutions.backendapirest.v1.repository.ComponentRepository;
import com.azenithsolutions.backendapirest.v1.repository.ItemRepository;
import com.azenithsolutions.backendapirest.v1.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ComponentRepository componentRepository;
    private final OrderRepository orderRepository;

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Optional<Item> getItemById(Long id) {
        return itemRepository.findById(id);
    }

    public List<Item> createItem(List<ItemRequestDTO> itemRequestDTOS) {
        System.out.println("Criando itens: " + itemRequestDTOS);

        itemRequestDTOS.forEach(dto -> {
            if (dto.getFkComponente() == null) {
                throw new IllegalArgumentException("ID do componente não pode ser nulo");
            }
            if (dto.getFkPedido() == null) {
                throw new IllegalArgumentException("ID do pedido não pode ser nulo");
            }
            if (dto.getQuantidade() == null || dto.getQuantidade() <= 0) {
                throw new IllegalArgumentException("Quantidade deve ser maior que zero");
            }
        });

        List<Item> items = convertDtoToEntity(itemRequestDTOS);

        System.out.println("Itens convertidos: " + items);
        return itemRepository.saveAll(items);
    }

    public Item updateItem(Long id, ItemRequestDTO itemRequestDTO) {
        Optional<Item> existingItemOpt = itemRepository.findById(id);

        if (existingItemOpt.isEmpty()) {
            return null;
        }

        Item updatedItem = convertDtoToUpdateEntity(itemRequestDTO);
        updatedItem.setIdItem(id);

        return itemRepository.save(updatedItem);
    }

    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    private List<Item> convertDtoToEntity(List<ItemRequestDTO> itemRequestDTOS) {
        List<Item> items = new ArrayList<>();

        for (ItemRequestDTO itemRequestDTO : itemRequestDTOS) {
            Item item = new Item();

            Component component = componentRepository.findById(itemRequestDTO.getFkComponente())
                    .orElseThrow(() -> new IllegalArgumentException("Component not found with ID: " + itemRequestDTO.getFkComponente()));
            item.setFkComponente(component);

            Order order = orderRepository.findById(itemRequestDTO.getFkPedido())
                    .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + itemRequestDTO.getFkPedido()));
            item.setFkPedido(order);

            item.setQuantidade(itemRequestDTO.getQuantidade());

            items.add(item);
        }

        return items;
    }

    private Item convertDtoToUpdateEntity(ItemRequestDTO itemRequestDTO) {
        Item item = new Item();

        Component component = componentRepository.findById(itemRequestDTO.getFkComponente())
                .orElseThrow(() -> new IllegalArgumentException("Component not found with ID: " + itemRequestDTO.getFkComponente()));

        item.setFkComponente(component);

        Order order = orderRepository.findById(itemRequestDTO.getFkPedido())
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + itemRequestDTO.getFkPedido()));
        item.setFkPedido(order);
        item.setQuantidade(itemRequestDTO.getQuantidade());
        return item;
    }
}
