package ru.alexanna.carcloud.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.alexanna.carcloud.dto.ItemDto;
import ru.alexanna.carcloud.entities.Item;
import ru.alexanna.carcloud.entities.ItemParameter;
import ru.alexanna.carcloud.service.Observable;
import ru.alexanna.carcloud.service.Observer;
import ru.alexanna.carcloud.service.services.ItemService;
import ru.alexanna.carcloud.service.services.MappingUtils;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1", produces = "application/json")
@RequiredArgsConstructor
public class ItemsController implements Observable {
    private final ItemService itemService;
    private final MappingUtils mappingUtils;

    private final List<Observer> observers = new ArrayList<>();

    @GetMapping("/items")
    public Iterable<ItemDto> findAllItems() {
        return itemService.findAllItems().stream().map(mappingUtils::mapToItemDto)
                .sorted(Comparator.comparing(ItemDto::getName))
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
        } catch (DataIntegrityViolationException e) {
            String errorMsg = Objects.nonNull(e.getRootCause()) ? e.getRootCause().getLocalizedMessage() : e.getLocalizedMessage();
            throw new ResponseStatusException(HttpStatus.CONFLICT, errorMsg);
        }
    }

    @PatchMapping(path = "/item/{id}", consumes = "application/json")
    public ItemDto patchItem(@PathVariable Long id, @RequestBody ItemDto patch) {
        Item item = itemService.findItem(id);
        if (Objects.nonNull(patch.getImei()))
            item.setImei(patch.getImei());
        if (Objects.nonNull(patch.getName()))
            item.setName(patch.getName());
        item.setPhoneNum1(patch.getPhoneNum1());
        item.setPhoneNum2(patch.getPhoneNum2());
        item.setDeviceType(patch.getDeviceType());
        item.setDescription(patch.getDescription());
        item.getParameters().clear();
        if (Objects.nonNull(patch.getParameters())) {
            Set<ItemParameter> patchedParameters = patch.getParameters().stream().map(itemParameterDto ->
                    mappingUtils.mapToItemParameter(itemParameterDto, item)).collect(Collectors.toSet());
            item.getParameters().addAll(patchedParameters);
        }
        try {
            Item patchedItem = itemService.save(item);
            notifyObservers(id);
            return mappingUtils.mapToItemDto(patchedItem);
        } catch (DataIntegrityViolationException e) {
            String errorMsg = Objects.nonNull(e.getRootCause()) ? e.getRootCause().getLocalizedMessage() : e.getLocalizedMessage();
            throw new ResponseStatusException(HttpStatus.CONFLICT, errorMsg);
        }
    }

    @DeleteMapping(path = "/item/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable Long id) {
        try {
            itemService.deleteItem(id);
            notifyObservers(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not deleted because missing");
        }
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Long id) {
        for (Observer observer: observers) {
            observer.updateItem(id);
        }
    }
}


