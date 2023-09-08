package ru.alexanna.carcloud.service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.alexanna.carcloud.entities.ItemParameter;
import ru.alexanna.carcloud.repositories.ItemParameterRepository;

import javax.transaction.Transactional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ItemParameterService {
    private final ItemParameterRepository itemParameterRepository;

    @Transactional
    public void deleteAllItemParameters(Long itemId) {
        itemParameterRepository.deleteItemParameterByItem_Id(itemId);
    }

    public Set<ItemParameter> findParameters(Long itemId) {
        return itemParameterRepository.findItemParametersByItem_Id(itemId);
    }
}
