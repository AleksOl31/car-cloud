package ru.alexanna.carcloud.service.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.alexanna.carcloud.dto.ItemDto;
import ru.alexanna.carcloud.entities.Item;
import ru.alexanna.carcloud.repositories.ItemRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final MappingUtils mappingUtils;

    public List<ItemDto> findAllItems() {
        return itemRepository.findAll().stream().map(mappingUtils::mapToItemDto)
                .collect(Collectors.toList());
    }

    public Optional<Item> findItemByImei(String imei) {
        return itemRepository.findByImei(imei);
    }

    public ItemDto findItemById(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,"Item with ID " + id + " not found"));
        return mappingUtils.mapToItemDto(item);
    }

    @SuppressWarnings("UnusedReturnValue")
    public Item save(Item item) {
        return itemRepository.save(item);
    }
}
