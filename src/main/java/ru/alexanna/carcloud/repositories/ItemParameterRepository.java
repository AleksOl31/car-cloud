package ru.alexanna.carcloud.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alexanna.carcloud.entities.ItemParameter;

import java.util.Set;

public interface ItemParameterRepository extends JpaRepository<ItemParameter, Long> {

    void deleteItemParameterByItem_Id(Long id);

    Set<ItemParameter> findItemParametersByItem_Id(Long id);
}
