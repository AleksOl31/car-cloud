package ru.alexanna.carcloud.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.alexanna.carcloud.dto.ItemDto;
import ru.alexanna.carcloud.entities.Item;
import ru.alexanna.carcloud.service.services.ItemService;
import ru.alexanna.carcloud.service.services.MappingUtils;

import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1", produces = "application/json")
@RequiredArgsConstructor
public class ItemsController {
    private final ItemService itemService;
    private final MappingUtils mappingUtils;

    @GetMapping("/items")
    public Iterable<ItemDto> findAllItems() {
        return itemService.findAllItems().stream().map(mappingUtils::mapToItemDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/item/{id}")
    public ItemDto findItemById(@PathVariable Long id) {
        Item item = itemService.findItem(id);
        return mappingUtils.mapToItemDto(item);
    }

    @PostMapping(path = "/items", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createNewItem(@RequestBody ItemDto itemDto) {
        itemDto.setConnectionState(false);
        try {
            Item newItem = itemService.createNewItem(itemDto);
            return mappingUtils.mapToItemDto(newItem);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @DeleteMapping(path = "/item/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable Long id) {
        try {
            itemService.deleteItem(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not deleted because missing");
        }
    }
}


