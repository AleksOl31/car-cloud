package ru.alexanna.carcloud.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.alexanna.carcloud.service.terminal.server.ServerState;

@Slf4j
@Controller
@AllArgsConstructor
public class HomeController {
    private ServerState serverState;

    @GetMapping("/home")
    public String home(Model model) {
        setModelAttributes(model);
        return "home";
    }

    @PostMapping("/home")
    public String changeState(Model model) {
        log.info("Terminal server state change running...");
        if (serverState.isRunning()){
            serverState.stop();
            log.info("Terminal server has stopped");
        } else {
            serverState.run();
            log.info("Terminal server is running");
        }
        setModelAttributes(model);
        return "home";
    }

    private void setModelAttributes(Model model) {
        model.addAttribute("state", serverState.getStateName());
        model.addAttribute("actionName", serverState.getActionName());
    }
}
