package ru.alexanna.carcloud.service.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.alexanna.carcloud.entities.TerminalMessage;
import ru.alexanna.carcloud.model.MonitoringPackage;
import ru.alexanna.carcloud.repositories.TerminalMessageRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MonitoringDataService {
    private final TerminalMessageRepository messageRepository;
    private final MappingUtils mappingUtils;

    public List<TerminalMessage> saveAll(List<MonitoringPackage> monitoringPackageList) {
        List<TerminalMessage> terminalMessageList = monitoringPackageList.stream()
                .map(mappingUtils::mapToTerminalMessage)
                .filter(terminalMessage -> terminalMessage.getImei() != null)
                .filter(terminalMessage -> terminalMessage.getCreatedAt() != null)
                .collect(Collectors.toList());
        return messageRepository.saveAll(terminalMessageList);
    }
}
