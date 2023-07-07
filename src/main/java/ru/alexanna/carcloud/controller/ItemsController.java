package ru.alexanna.carcloud.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.alexanna.carcloud.dto.ItemDto;
import ru.alexanna.carcloud.service.services.ItemService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemsController {
    private final ItemService itemService;

    @GetMapping("api/v1/items")
    public List<ItemDto> findAllItems() {
        return itemService.findAllItems();
    }

    @GetMapping("api/v1/item/{id}")
    public ItemDto findItemById(@PathVariable Long id) {
        return itemService.findItemById(id);
    }
}
