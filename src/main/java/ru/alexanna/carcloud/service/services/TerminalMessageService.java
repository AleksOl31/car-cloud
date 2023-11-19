package ru.alexanna.carcloud.service.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.alexanna.carcloud.entities.Item;
import ru.alexanna.carcloud.entities.TerminalMessage;
import ru.alexanna.carcloud.dto.MonitoringPackage;
import ru.alexanna.carcloud.repositories.TerminalMessageRepository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor

public class TerminalMessageService {
    private final TerminalMessageRepository terminalMessageRepository;
    private final MappingUtils mappingUtils;

    @SuppressWarnings("UnusedReturnValue")
    public List<TerminalMessage> saveAll(List<MonitoringPackage> monitoringPackageList, Item item) {
        List<TerminalMessage> terminalMessageList = monitoringPackageList.stream()
                .map(monitoringPackage -> mappingUtils.mapToTerminalMessage(monitoringPackage, item))
                .collect(Collectors.toList());
        List<TerminalMessage> result = new ArrayList<>();
        for (TerminalMessage terminalMessage : terminalMessageList) {
            result.add(this.save(terminalMessage));
        }
        return result;
    }

    public TerminalMessage save(TerminalMessage terminalMessage) {
        TerminalMessage tm = null;
        try {
            tm = terminalMessageRepository.save(terminalMessage);
        } catch (DataIntegrityViolationException e) {
            String errorMsg = Objects.nonNull(e.getRootCause()) ? e.getRootCause().getLocalizedMessage() : e.getLocalizedMessage();
            log.error("Database save error: {}", errorMsg);
        } catch (RuntimeException e) {
            log.error(e.getLocalizedMessage());
        }
        return tm;
    }

    /**
     * Получить сообщения за последний час от itemId.
     *
     * @return Список сообщений {@code List<MonitoringPackage>} .
     * @see #findTerminalMessages
     */
    public List<MonitoringPackage> findTerminalMessagesLastHour(Long itemId) {
        // Найти временную точку, меньшую на 1 ч (например, от текущей)

/*        Date currentTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        calendar.add(Calendar.HOUR, -1);
        Date timeFrom = calendar.getTime();  */

        long timeTo = System.currentTimeMillis();
        long timeFrom = timeTo - 3600 * 1000;
        return findTerminalMessages(itemId, timeFrom, timeTo);
    }

    public List<MonitoringPackage> findTerminalMessagesLastMinute(Long itemId) {
        long timeTo = System.currentTimeMillis();
        long timeFrom = timeTo - 60 * 1000;
        return findTerminalMessages(itemId, timeFrom, timeTo);
    }

    public List<MonitoringPackage> findTerminalMessages(Long itemId, Long timeFrom, Long timeTo) {
        Date tFrom = new Date(timeFrom);
        Date tTo = new Date(timeTo);
        List<TerminalMessage> terminalMessages = terminalMessageRepository
                .findTerminalMessagesByItemIdAndCreatedAtBetweenOrderByCreatedAtDesc(itemId, tFrom, tTo);
        return mapToMonitoringPackages(terminalMessages);
    }

    public List<TerminalMessage> filterOutNullValues(List<TerminalMessage> terminalMessages, double filterThreshold) {
        for (int i = 0; i < terminalMessages.size(); i++) {
            List<Double> extendedTags = terminalMessages.get(i).getExtendedTags();
            for (int j = 0; j < extendedTags.size(); j++) {
                if (extendedTags.get(j).equals(0.0) && terminalMessages.size() > 1) {
                    if (i == 0) {
                        final double nextVal = terminalMessages.get(i + 1).getExtendedTags().get(j);
                        if (Math.abs(nextVal) > filterThreshold)
                            extendedTags.set(j, nextVal);
                    } else if (i < terminalMessages.size() - 1) {
                        final double previousVal = terminalMessages.get(i - 1).getExtendedTags().get(j);
                        final double nextVal = terminalMessages.get(i + 1).getExtendedTags().get(j);
                        if (previousVal > 0 && nextVal > 0) {
                            if (previousVal > filterThreshold && nextVal > filterThreshold) {
                                double newValue = previousVal + (nextVal - previousVal) / 2;
                                extendedTags.set(j, newValue);
                            }
                        }
                    } else {
                        final double previousVal = terminalMessages.get(i - 1).getExtendedTags().get(j);
                        if (Math.abs(previousVal) > filterThreshold)
                            extendedTags.set(j, previousVal);
                    }
                }
            }
        }
        return terminalMessages;
    }

    private List<MonitoringPackage> mapToMonitoringPackages(List<TerminalMessage> terminalMessages) {
        return terminalMessages.stream().map(mappingUtils::mapToMonitoringPackage).collect(Collectors.toList());
    }
}
