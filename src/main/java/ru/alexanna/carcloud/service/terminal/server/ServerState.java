package ru.alexanna.carcloud.service.terminal.server;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.alexanna.carcloud.service.services.InetConnectionTestService;
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
    private final InetConnectionTestService inetConnectionTestService;

    public ServerState(BaseNettyServer baseNettyServer, InetConnectionTestService inetConnectionTestService) {
        this.baseNettyServer = baseNettyServer;
        this.inetConnectionTestService = inetConnectionTestService;
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
        startInetConnectionTesting();
    }

    public void stop() {
        baseNettyServer.stop();
        setRunning(false);
        stopInetConnectionTesting();
    }

    public void startInetConnectionTesting() {
        inetConnectionTestService.start();
    }

    public void stopInetConnectionTesting() {
        inetConnectionTestService.stop();
    }
}
