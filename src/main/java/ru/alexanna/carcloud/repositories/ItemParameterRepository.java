package ru.alexanna.carcloud.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alexanna.carcloud.entities.ItemParameter;

public interface ItemParameterRepository extends JpaRepository<ItemParameter, Long> {
}
