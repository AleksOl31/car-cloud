package ru.alexanna.carcloud.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.alexanna.carcloud.service.terminal.server.BaseNettyServer;

@Slf4j
@Service
public class ServerState {
    @Getter
    @Setter
    private boolean running = false;
    private String stateName;
    private String actionName;
    private final BaseNettyServer baseNettyServer;
    private Thread thread;

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
        setRunning(true);
        thread = new Thread(baseNettyServer::run);
        thread.start();
    }

    public void stop() {
            setRunning(false);
            baseNettyServer.stop();
    }
}
