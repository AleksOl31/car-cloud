package ru.alexanna.carcloud.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alexanna.carcloud.entities.InetCrashEvent;

public interface InetCrashRepository extends JpaRepository<InetCrashEvent, Long> {
}
