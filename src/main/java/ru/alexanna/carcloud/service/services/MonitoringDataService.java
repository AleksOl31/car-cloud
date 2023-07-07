package ru.alexanna.carcloud.service.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.alexanna.carcloud.dto.ItemDto;
import ru.alexanna.carcloud.entities.Item;
import ru.alexanna.carcloud.entities.TerminalMessage;
import ru.alexanna.carcloud.dto.MonitoringPackage;
import ru.alexanna.carcloud.repositories.ItemRepository;
import ru.alexanna.carcloud.repositories.TerminalMessageRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor

public class MonitoringDataService {
    private final TerminalMessageRepository terminalMessageRepository;
    private final ItemRepository itemRepository;
    private final MappingUtils mappingUtils;

    @SuppressWarnings("UnusedReturnValue")
    public List<TerminalMessage> saveAll(List<MonitoringPackage> monitoringPackageList, Item item) {
        List<TerminalMessage> terminalMessageList = monitoringPackageList.stream()
                .map(monitoringPackage -> mappingUtils.mapToTerminalMessage(monitoringPackage, item))
//                .filter(terminalMessage -> Objects.nonNull(terminalMessage.getImei()))
//                .filter(terminalMessage -> Objects.nonNull(terminalMessage.getCreatedAt()))
                .collect(Collectors.toList());
        return terminalMessageRepository.saveAll(terminalMessageList);
    }

    public List<ItemDto> findAllItems() {
        return itemRepository.findAll().stream().map(mappingUtils::mapToItemDto)
                .collect(Collectors.toList());
    }

    public Optional<Item> findItemByImei(String imei) {
        return itemRepository.findByImei(imei);
    }

    public ItemDto findItemById(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,"Item with ID " + id + " not found"));
        return mappingUtils.mapToItemDto(item);
    }

    // FIXME привести рефакторинг (использовать findTerminalMessages(Long, Long, Long))
    public List<MonitoringPackage> findTerminalMessagesLastHour(Long itemId) {
        Date currentTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        calendar.add(Calendar.HOUR, -1);
        Date timeFrom = calendar.getTime();
        List<TerminalMessage> terminalMessages = terminalMessageRepository
                .findTerminalMessagesByItemIdAndCreatedAtBetweenOrderByCreatedAtDesc(itemId, timeFrom, currentTime);
        return mapToMonitoringPackage(terminalMessages);
    }

    public List<MonitoringPackage> findTerminalMessages(Long itemId, Long timeFrom, Long timeTo) {
        Date tFrom = new Date(timeFrom);
        Date tTo = new Date(timeTo);
        List<TerminalMessage> terminalMessages = terminalMessageRepository
                .findTerminalMessagesByItemIdAndCreatedAtBetweenOrderByCreatedAtDesc(itemId, tFrom, tTo);
        return mapToMonitoringPackage(terminalMessages);
    }

    private List<MonitoringPackage> mapToMonitoringPackage(List<TerminalMessage> terminalMessages) {
        return terminalMessages.stream().map(mappingUtils::mapToMonitoringPackage).collect(Collectors.toList());
    }
}
