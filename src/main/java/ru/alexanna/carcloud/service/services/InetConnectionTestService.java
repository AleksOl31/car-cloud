package ru.alexanna.carcloud.service.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import ru.alexanna.carcloud.configuration.InetConnectionTestProperties;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Timer;
import java.util.TimerTask;

@Service
@PropertySource("classpath:connection.properties")
@Slf4j
public class InetConnectionTestService {

    private final InetConnectionTestProperties testProperties;
/*    @Value("${inet.testing.hostname1}")
    private String hostname1;
    @Value("${inet.testing.hostname2}")
    private String hostname2;
    @Value("${inet.testing.timeout}")
    private int testTimeout;
    @Value("${inet.testing.period}")
    private long testPeriod;*/

    public InetConnectionTestService(InetConnectionTestProperties testProperties) {
        this.testProperties = testProperties;
        Timer inetTestTimer = new Timer("InetTestingTimer", true);
        TimerTask inetTestingTask = createInetTestingTask();
        inetTestTimer.scheduleAtFixedRate(inetTestingTask,5000, 5000);
    }

    private TimerTask createInetTestingTask() {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    boolean isHostname1Reachable = InetAddress.getByName(testProperties.getHostname1()).isReachable(1000);
                    boolean isHostname2Reachable = InetAddress.getByName(testProperties.getHostname2()).isReachable(1000);
                    if (isHostname1Reachable || isHostname2Reachable)
                        performReachableAction();
                    else
                        performUnreachableAction();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    private static void performReachableAction() {
        log.debug("Internet connection available {}", System.currentTimeMillis());
    }

    private static void performUnreachableAction() {
        log.error("Internet connection unavailable {}", System.currentTimeMillis());
    }
}
