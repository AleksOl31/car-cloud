package ru.alexanna.carcloud.model;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Component
public class ServerState {
    @Getter
    private boolean running;
//    @Value("запущен")
    private String stateName;
//    @Value("Остановить")
    private String actionName;

    public void setRunning(boolean isRunning) {
        running = isRunning;
    }

    public String getStateName() {
        return isRunning() ? "запущен" : "остановлен";
    }

    public String getActionName() {
        return isRunning() ? "Остановить" : "Запустить";
    }
}
