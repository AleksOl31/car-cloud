package ru.alexanna.carcloud.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alexanna.carcloud.entities.ItemParameter;

import javax.transaction.Transactional;

public interface ItemParameterRepository extends JpaRepository<ItemParameter, Long> {
    @Transactional
    void deleteItemParameterByItem_Id(Long id);
}
