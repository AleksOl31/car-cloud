package ru.alexanna.carcloud.service.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.alexanna.carcloud.entities.Item;
import ru.alexanna.carcloud.entities.TerminalMessage;
import ru.alexanna.carcloud.dto.MonitoringPackage;
import ru.alexanna.carcloud.repositories.ItemRepository;
import ru.alexanna.carcloud.repositories.TerminalMessageRepository;

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

/*    public List<Item> findAllItems() {
        return itemRepository.findAll();
    }*/

    public Optional<Item> findItemByImei(String imei) {
        return itemRepository.findByImei(imei);
    }
}
