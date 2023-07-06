package ru.alexanna.carcloud.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alexanna.carcloud.entities.TerminalMessage;

import java.util.Date;
import java.util.List;

@Repository
public interface TerminalMessageRepository extends JpaRepository<TerminalMessage, Long> {
    List<TerminalMessage> findTerminalMessagesByItemIdAndCreatedAtBetweenOrderByCreatedAtDesc(Long item_id, Date timeFrom, Date timeTo);
}
