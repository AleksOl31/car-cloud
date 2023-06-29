package ru.alexanna.carcloud.service.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.alexanna.carcloud.entities.TerminalMessage;
import ru.alexanna.carcloud.dto.MonitoringPackage;
import ru.alexanna.carcloud.repositories.TerminalMessageRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor

public class MonitoringDataService {
    private final TerminalMessageRepository terminalMessageRepository;
    private final MappingUtils mappingUtils;

    @SuppressWarnings("UnusedReturnValue")
    public List<TerminalMessage> saveAll(List<MonitoringPackage> monitoringPackageList) {
        List<TerminalMessage> terminalMessageList = monitoringPackageList.stream()
                .map(mappingUtils::mapToTerminalMessage)
//                .filter(terminalMessage -> Objects.nonNull(terminalMessage.getImei()))
//                .filter(terminalMessage -> Objects.nonNull(terminalMessage.getCreatedAt()))
                .collect(Collectors.toList());
        return terminalMessageRepository.saveAll(terminalMessageList);
    }
}
