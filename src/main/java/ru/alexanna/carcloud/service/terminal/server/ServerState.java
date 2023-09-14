package ru.alexanna.carcloud.service.terminal.server;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import ru.alexanna.carcloud.service.services.InetConnectionTestService;

@Slf4j
@Service
@PropertySource("classpath:connection.properties")
public class ServerState {
    @Getter
    @Setter
    private boolean running = false;
    @Value("${inet.testing.activate}")
    private boolean isActivate;

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
        if (isActivate)
            startInetConnectionTesting();
    }

    public void stop() {
        baseNettyServer.stop();
        setRunning(false);
        if (isActivate)
            stopInetConnectionTesting();
    }

    public void startInetConnectionTesting() {
        inetConnectionTestService.start();
    }

    public void stopInetConnectionTesting() {
        inetConnectionTestService.stop();
    }
}
