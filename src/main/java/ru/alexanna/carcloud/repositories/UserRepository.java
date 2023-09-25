package ru.alexanna.carcloud.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.alexanna.carcloud.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
