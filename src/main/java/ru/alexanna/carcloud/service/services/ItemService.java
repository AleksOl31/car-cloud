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

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final MappingUtils mappingUtils;

    public List<Item> findAllItems() {
        return itemRepository.findAll();
    }

    public Optional<Item> findItem(String imei) {
        return itemRepository.findByImei(imei);
    }

    public Item findItem(Long id) {
        return itemRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,"Item with ID " + id + " not found"));
    }

    @SuppressWarnings("UnusedReturnValue")
    public Item save(Item item) {
        return itemRepository.save(item);
    }

    public Item createNewItem(ItemDto itemDto) {
        Item item = mappingUtils.mapToItem(itemDto);
        return save(item);
    }

    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }
}
