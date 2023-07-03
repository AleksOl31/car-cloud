package ru.alexanna.carcloud.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alexanna.carcloud.entities.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
