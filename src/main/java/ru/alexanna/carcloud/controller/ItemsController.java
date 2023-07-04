package ru.alexanna.carcloud.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.alexanna.carcloud.dto.ItemDto;
import ru.alexanna.carcloud.service.services.MappingUtils;
import ru.alexanna.carcloud.service.services.MonitoringDataService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ItemsController {
    private final MonitoringDataService monitoringDataService;
    private final MappingUtils mappingUtils;

    @GetMapping("api/v1/items")
    public List<ItemDto> findAllItems() {
        return monitoringDataService.findAllItems().stream().map(mappingUtils::mapToItemDto)
                .collect(Collectors.toList());
    }
}
