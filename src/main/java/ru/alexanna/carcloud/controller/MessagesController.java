package ru.alexanna.carcloud.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.alexanna.carcloud.aspects.RequestMessages;
import ru.alexanna.carcloud.dto.MonitoringPackage;
import ru.alexanna.carcloud.service.services.TerminalMessageService;


@RestController
@CrossOrigin(origins = "*") //(origins = "http://10.70.29.158:8189")
@RequestMapping(path = "api/v1/messages", produces = "application/json")
@RequiredArgsConstructor
public class MessagesController {
    private final TerminalMessageService terminalMessageService;
// FIXME: Маппинг в DTO (MonitoringPackage) делать, наверное, тут
    @GetMapping("/load-last-hour/{itemId}")
    @RequestMessages
    public Iterable<MonitoringPackage> loadMessagesLastHour(@PathVariable Long itemId) {
        return terminalMessageService.findTerminalMessagesLastHour(itemId);
    }

    @GetMapping("/load-last-minute/{itemId}")
    @RequestMessages
    public Iterable<MonitoringPackage> loadMessagesLastMinute(@PathVariable Long itemId) {
        return terminalMessageService.findTerminalMessagesLastMinute(itemId);
    }

    @GetMapping("/{itemId}")
    @RequestMessages
    public Iterable<MonitoringPackage> loadMessagesInterval(@PathVariable Long itemId, @RequestParam Long timeFrom, @RequestParam Long timeTo) {
        return terminalMessageService.findTerminalMessages(itemId, timeFrom, timeTo);
    }
}
