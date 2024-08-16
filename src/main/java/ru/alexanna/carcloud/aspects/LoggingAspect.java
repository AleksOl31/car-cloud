package ru.alexanna.carcloud.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.alexanna.carcloud.dto.MonitoringPackage;

import java.util.Arrays;
import java.util.List;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("@annotation(RequestMessages)")
    @SuppressWarnings("unchecked")
    public Object logMessageRequests(ProceedingJoinPoint joinPoint) throws Throwable {
        long itemId = (long) Arrays.stream(joinPoint.getArgs()).findFirst().orElse(-1);
        List<MonitoringPackage> messages = (List<MonitoringPackage>) joinPoint.proceed();
        log.info("Request ({}) to receive messages completed: {} messages sent from item ID {}"
                , joinPoint.getSignature().getName(), messages.size(), itemId);
        return messages;
    }
}
