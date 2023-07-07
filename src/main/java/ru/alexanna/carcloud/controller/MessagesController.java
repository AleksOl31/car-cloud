package ru.alexanna.carcloud.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.alexanna.carcloud.dto.MonitoringPackage;
import ru.alexanna.carcloud.service.services.MonitoringDataService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MessagesController {
    private final MonitoringDataService monitoringDataService;

    @GetMapping("api/v1/messages/load-last-hour/{id}")
    public List<MonitoringPackage> loadMessagesLastHour(@PathVariable Long id) {
        return monitoringDataService.findTerminalMessagesLastHour(id);
    }

    @GetMapping("api/v1/messages/{id}")
    public List<MonitoringPackage> loadMessagesInterval(@PathVariable Long id, @RequestParam Long timeFrom, @RequestParam Long timeTo) {
        return monitoringDataService.findTerminalMessages(id, timeFrom, timeTo);
    }
}
