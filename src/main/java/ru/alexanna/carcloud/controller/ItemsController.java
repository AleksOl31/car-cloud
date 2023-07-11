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
//@CrossOrigin(origins = {"http://carcloud:8189"})
@RequiredArgsConstructor
public class ItemsController {
    private final ItemService itemService;
    private final MappingUtils mappingUtils;

    @GetMapping("api/v1/items")
    public Iterable<ItemDto> findAllItems() {
        return itemService.findAllItems().stream().map(mappingUtils::mapToItemDto)
                .collect(Collectors.toList());
    }

    @GetMapping("api/v1/item/{id}")
    public ItemDto findItemById(@PathVariable Long id) {
        Item item = itemService.findItem(id);
        return mappingUtils.mapToItemDto(item);
    }

    @PostMapping(path = "api/v1/items", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createNewItem(@RequestBody ItemDto itemDto) {
        itemDto.setConnectionState(false);
        try {
            Item item = itemService.createNewItem(itemDto);
            return mappingUtils.mapToItemDto(item);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @DeleteMapping(path = "api/v1/item/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable Long id) {
        try {
            itemService.deleteItem(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entry not deleted because missing");
        }
    }
}


