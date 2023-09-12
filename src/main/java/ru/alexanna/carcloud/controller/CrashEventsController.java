package ru.alexanna.carcloud.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.alexanna.carcloud.entities.InetCrashEvent;
import ru.alexanna.carcloud.service.services.InetConnectionTestService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1", produces = "application/json")
@RequiredArgsConstructor
public class CrashEventsController {
    private final InetConnectionTestService testService;

    @GetMapping("/crash-events")
    public Iterable<InetCrashEvent> findAllEvents() {
        return testService.findAllEvents();
    }
}
