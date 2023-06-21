package ru.alexanna.carcloud.service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.alexanna.carcloud.entities.TerminalMessage;
import ru.alexanna.carcloud.model.MonitoringPackage;
import ru.alexanna.carcloud.repositories.TerminalMessageRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MonitoringDataService {
    private final TerminalMessageRepository messageRepository;
    private final MappingUtils mappingUtils;

    public void save(List<MonitoringPackage> monitoringPackageList) {
        List<TerminalMessage> terminalMessageList = monitoringPackageList.stream()
                .map(mappingUtils::mapToTerminalMessage).collect(Collectors.toList());
        messageRepository.saveAll(terminalMessageList);
    }
}
