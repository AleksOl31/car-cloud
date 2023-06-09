package ru.alexanna.carcloud.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.alexanna.carcloud.model.ServerState;
import ru.alexanna.carcloud.service.terminal.server.BaseNettyServer;

@Slf4j
@Controller
@AllArgsConstructor
public class HomeController {
    private ServerState serverState;
    private BaseNettyServer baseNettyServer;

    @GetMapping("/home")
    public String viewState(Model model) {
        model.addAttribute("state", serverState.getStateName());
        model.addAttribute("actionName", serverState.getActionName());
        return "home";
    }

    @PostMapping("/home")
    public String changeState(Model model) {
        log.debug("POST");
        if (serverState.isRunning())
            baseNettyServer.stop();
        else
            baseNettyServer.run();
        model.addAttribute("state", serverState.getStateName());
        model.addAttribute("actionName", serverState.getActionName());
        return "home";
    }
}
