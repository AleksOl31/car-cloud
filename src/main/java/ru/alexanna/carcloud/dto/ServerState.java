package ru.alexanna.carcloud.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.alexanna.carcloud.service.terminal.server.BaseNettyServer;

@Slf4j
@Service
public class ServerState {
    @Getter
    @Setter
    private boolean running = false;
//    private String stateName;
//    private String actionName;
    private final BaseNettyServer baseNettyServer;

    public ServerState(BaseNettyServer baseNettyServer) {
        this.baseNettyServer = baseNettyServer;
    }

    public String getStateName() {
        return isRunning() ? "запущен" : "остановлен";
    }

    public String getActionName() {
        return isRunning() ? "Остановить" : "Запустить";
    }

    public void run() {
        Thread thread = new Thread(baseNettyServer);
        thread.start();
        setRunning(true);
    }

    public void stop() {
        baseNettyServer.stop();
        setRunning(false);
    }
}
