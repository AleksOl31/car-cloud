package ru.alexanna.carcloud.service.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.alexanna.carcloud.dto.MonitoringPackage;
import ru.alexanna.carcloud.entities.Item;
import ru.alexanna.carcloud.entities.TerminalMessage;
import ru.alexanna.carcloud.repositories.TerminalMessageRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
        List<TerminalMessage> filteredTerminalMessages = filterOutNullValues(terminalMessages, 2);
        return mapToMonitoringPackages(filteredTerminalMessages);
    }

    public List<TerminalMessage> filterOutNullValues(List<TerminalMessage> messages, double filterThreshold) {
        for (int i = 0; i < messages.size(); i++) {
            List<Double> extendedTags = messages.get(i).getExtendedTags();
            for (int j = 0; j < extendedTags.size(); j++) {
                if (extendedTags.get(j).equals(0.0) && messages.size() > 1) {
                    if (i == 0) {
                        final double nextVal = getNextMsgValue(messages, i, j);
                        if (Math.abs(nextVal) > filterThreshold)
                            extendedTags.set(j, nextVal);
                    } else if (i < messages.size() - 1) {
                        final double previousVal = getPreviousMsgValue(messages, i, j);
                        final double nextVal = getNextMsgValue(messages, i, j);
                        if (previousVal > filterThreshold && nextVal > filterThreshold) {
                            final double newValue = previousVal + (nextVal - previousVal) / 2;
                            extendedTags.set(j, newValue);
                        }
                    } else {
                        final double previousVal = getPreviousMsgValue(messages, i, j);
                        if (Math.abs(previousVal) > filterThreshold)
                            extendedTags.set(j, previousVal);
                    }
                }
            }
        }
        return messages;
    }

    private static double getNextMsgValue(List<TerminalMessage> messages, int currentMsgIndex, int currentTagIndex) {
        return messages.get(currentMsgIndex + 1).getExtendedTags().get(currentTagIndex);
    }

    private static double getPreviousMsgValue(List<TerminalMessage> messages, int currentMsgIndex, int currentTagIndex) {
        return messages.get(currentMsgIndex - 1).getExtendedTags().get(currentTagIndex);
    }

    private List<MonitoringPackage> mapToMonitoringPackages(List<TerminalMessage> terminalMessages) {
        return terminalMessages.stream().map(mappingUtils::mapToMonitoringPackage).collect(Collectors.toList());
    }
}
