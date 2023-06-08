package ru.alexanna.carcloud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.alexanna.carcloud.model.ServerState;

@Controller
public class HomeController {
//    private ServerState serverState = new ServerState(true, "запущен", "Остановить");
    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("state", "запущен");
        return "home.html";
    }
}
