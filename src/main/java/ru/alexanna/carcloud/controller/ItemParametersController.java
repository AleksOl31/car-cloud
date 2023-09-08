package ru.alexanna.carcloud.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.alexanna.carcloud.dto.ItemParameterDto;
import ru.alexanna.carcloud.service.services.ItemParameterService;
import ru.alexanna.carcloud.service.services.MappingUtils;

import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/parameters", produces = "application/json")
@RequiredArgsConstructor
public class ItemParametersController {
    private final ItemParameterService itemParameterService;
    private final MappingUtils mappingUtils;

    @GetMapping("/{itemId}")
    public Iterable<ItemParameterDto> findParameters(@PathVariable Long itemId) {
        return itemParameterService.findParameters(itemId).stream()
                .map(mappingUtils::mapToItemParameterDto).collect(Collectors.toSet());
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteParameters(@PathVariable Long itemId) {
        try {
            itemParameterService.deleteAllItemParameters(itemId);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Parameters not deleted because item missing");
        }
    }
}
