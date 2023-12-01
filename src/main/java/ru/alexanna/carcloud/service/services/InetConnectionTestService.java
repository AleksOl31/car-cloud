package ru.alexanna.carcloud.service.services;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import ru.alexanna.carcloud.entities.InetCrashEvent;
import ru.alexanna.carcloud.repositories.InetCrashRepository;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Service
@PropertySource("classpath:connection.properties")
@Slf4j
@RequiredArgsConstructor
@Getter
public class InetConnectionTestService {

    @Value("${inet.testing.hostname1:google.com}")
    private String hostname1;
    @Value("${inet.testing.hostname2:ya.ru}")
    private String hostname2;
    @Value("${inet.testing.port:80}")
    private int testPort;
    @Value("${inet.testing.timeout:1000}")
    private int testTimeout;
    @Value("${inet.testing.period:30000}")
    private long testPeriod;
    private Timer inetTestTimer;
    private final InetCrashRepository crashRepository;
    private InetCrashEvent currentCrashEvent;

    private TimerTask createInetTestingTask() {
        return new TimerTask() {
            @Override
            public void run() {
                boolean isHost1Reachable = isSocketAddressReachable(hostname1, testPort, testTimeout);
                boolean isHost2Reachable = isSocketAddressReachable(hostname2, testPort, testTimeout);
                if (isHost1Reachable || isHost2Reachable)
                    performReachableAction(isHost1Reachable, isHost2Reachable);
                else
                    performUnreachableAction();
            }
        };
    }

    private static boolean isSocketAddressReachable(String address, int port, int timeout) {
        Socket testSocket = new Socket();
        try {
            testSocket.connect(new InetSocketAddress(address, port), timeout);
            return true;
        } catch (IOException exception) {
            log.error("A network error has occurred with the message: {}", exception.getMessage());
            return false;
        } finally {
            try {
                testSocket.close();
            } catch (IOException e) {
                log.error("Socket {} closing error...", testSocket.getInetAddress());
            }
        }
    }

    private void performReachableAction(boolean isHost1Reached, boolean isHost2Reached) {
        if (currentCrashEvent != null) {
            log.info("Internet connection available: {} - {}, {} - {} at {}", hostname1, isHost1Reached, hostname2, isHost2Reached, new Date());
            currentCrashEvent.setEndAt(new Date(System.currentTimeMillis()));
            crashRepository.save(currentCrashEvent);
            currentCrashEvent = null;
        }
    }

    private void performUnreachableAction() {
        log.error("Internet is not available at {}", new Date());
        if (currentCrashEvent == null) {
            InetCrashEvent newCrashEvent = new InetCrashEvent();
            newCrashEvent.setStartAt(new Date(System.currentTimeMillis()));
            currentCrashEvent = crashRepository.save(newCrashEvent);
        }
    }

    public void start() {
        inetTestTimer = new Timer("InetTestingTimer", true);
        TimerTask inetTestingTask = createInetTestingTask();
        inetTestTimer.scheduleAtFixedRate(inetTestingTask, 1000, testPeriod);
        log.info("Inet testing timer started at {}", new Date());
    }

    public void stop() {
        inetTestTimer.cancel();
        log.info("Inet test timer stopped at {}", new Date());
    }

    public List<InetCrashEvent> findAllEvents() {
        return crashRepository.findAll();
    }
}
