package ru.alexanna.carcloud.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alexanna.carcloud.entities.TerminalMessage;

@Repository
public interface TerminalMessageRepository extends JpaRepository<TerminalMessage, Long> {
}
