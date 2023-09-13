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
import java.net.InetAddress;
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

    @Value("${inet.testing.hostname1}")
    private String hostname1;
    @Value("${inet.testing.hostname2}")
    private String hostname2;
    @Value("${inet.testing.timeout:1000}")
    private int testTimeout;
    @Value("${inet.testing.period:5000}")
    private long testPeriod;
    private Timer inetTestTimer;
    private final InetCrashRepository crashRepository;
    private InetCrashEvent currentCrashEvent;

    private TimerTask createInetTestingTask() {
        return new TimerTask() {
            @Override
            public void run() {
                InetAddress inetAddress1 = null;
                InetAddress inetAddress2 = null;
                try {
                    inetAddress1 = InetAddress.getByName(hostname1);
                    inetAddress2 = InetAddress.getByName(hostname2);
                    boolean is1Reached = inetAddress1.isReachable(testTimeout);
                    boolean is2Reached = inetAddress2.isReachable(testTimeout);
                    if (is1Reached || is2Reached)
                        performReachableAction(inetAddress1, inetAddress2, is1Reached, is2Reached);
                    else
                        performUnreachableAction(inetAddress1, inetAddress2);
                } catch (IOException e) {
//                    throw new RuntimeException(e);
                    performUnreachableAction(inetAddress1, inetAddress2);
                    e.printStackTrace();
                }
            }
        };
    }

    private void performReachableAction(InetAddress inetAddress1, InetAddress inetAddress2, boolean isInet1Reached, boolean isInet2Reached) {
        if (currentCrashEvent != null) {
            log.debug("Internet connection available: {} - {}, {} - {}", inetAddress1, isInet1Reached, inetAddress2, isInet2Reached);
            currentCrashEvent.setEndAt(new Date(System.currentTimeMillis()));
            crashRepository.save(currentCrashEvent);
            currentCrashEvent = null;
        }
    }

    private void performUnreachableAction(InetAddress inetAddress1, InetAddress inetAddress2) {
        log.error("Internet hosts {} and {} are not reachable: ", inetAddress1, inetAddress2);
        if (currentCrashEvent == null) {
            InetCrashEvent newCrashEvent = new InetCrashEvent();
            newCrashEvent.setStartAt(new Date(System.currentTimeMillis()));
            currentCrashEvent = crashRepository.save(newCrashEvent);
        }
    }

    public void start() {
        inetTestTimer = new Timer("InetTestingTimer", true);
        TimerTask inetTestingTask = createInetTestingTask();
        inetTestTimer.scheduleAtFixedRate(inetTestingTask,1000, testPeriod);
        log.info("Inet testing timer started");
    }

    public void stop() {
        inetTestTimer.cancel();
        log.info("Inet test timer stopped");
    }

    public List<InetCrashEvent> findAllEvents() {
        return crashRepository.findAll();
    }
}
