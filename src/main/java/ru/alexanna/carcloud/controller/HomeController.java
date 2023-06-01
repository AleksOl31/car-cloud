package ru.alexanna.carcloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.alexanna.carcloud.model.Car;

@Slf4j
@Controller
@RequestMapping("/car-cloud")
public class HomeController {
    private final Car car;
//    private Logger logger = LogManager.getLogger(HomeController.class);

    public HomeController(Car car) {
        this.car = car;
    }

    @GetMapping
    public String home(Model model) {
        log.info("Home page started");
        model.addAttribute("car", car);
        return "home";
    }
}
