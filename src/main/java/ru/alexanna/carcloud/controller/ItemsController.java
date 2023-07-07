package ru.alexanna.carcloud.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.alexanna.carcloud.dto.ItemDto;
import ru.alexanna.carcloud.service.services.MappingUtils;
import ru.alexanna.carcloud.service.services.MonitoringDataService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ItemsController {
    private final MonitoringDataService monitoringDataService;

    @GetMapping("api/v1/items")
    public List<ItemDto> findAllItems() {
        return monitoringDataService.findAllItems();
    }

    @GetMapping("api/v1/item/{id}")
    public ItemDto findItemById(@PathVariable Long id) {
        return monitoringDataService.findItemById(id);
    }
}
