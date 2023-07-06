package ru.alexanna.carcloud.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.alexanna.carcloud.dto.MonitoringPackage;
import ru.alexanna.carcloud.entities.TerminalMessage;
import ru.alexanna.carcloud.service.services.MonitoringDataService;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MessagesController {
    private final MonitoringDataService monitoringDataService;

    @GetMapping("api/v1/messages/load-last-hour/{id}")
    public List<MonitoringPackage> loadMessagesLastHour(@PathVariable Long id) {
        return monitoringDataService.findTerminalMessagesLastHour(id);
    }
}
