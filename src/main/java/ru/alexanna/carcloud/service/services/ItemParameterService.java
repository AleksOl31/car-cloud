package ru.alexanna.carcloud.service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.alexanna.carcloud.repositories.ItemParameterRepository;

@Service
@RequiredArgsConstructor
public class ItemParameterService {
    private final ItemParameterRepository itemParameterRepository;
}
