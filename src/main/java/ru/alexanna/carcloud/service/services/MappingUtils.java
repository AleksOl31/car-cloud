package ru.alexanna.carcloud.service.services;

import org.springframework.stereotype.Service;
import ru.alexanna.carcloud.entities.TerminalMessage;
import ru.alexanna.carcloud.model.MonitoringPackage;

import java.util.Optional;

@Service
public class MappingUtils {
    public TerminalMessage mapToTerminalMessage(MonitoringPackage monitoringPackage) {
        return TerminalMessage.builder()
                .imei(monitoringPackage.getRegInfo().getImei())
                .deviceId(monitoringPackage.getRegInfo().getDeviceId())
                .hardVer(monitoringPackage.getRegInfo().getHardVer())
                .softVer(monitoringPackage.getRegInfo().getSoftVer())
                .recordNum(monitoringPackage.getRecordNum())
                .supplyVol(monitoringPackage.getSupplyVol())
                .batteryVol(monitoringPackage.getBatteryVol())
                .deviceTemp(monitoringPackage.getDeviceTemp())
                .status(monitoringPackage.getStatus())
                .createdAt(monitoringPackage.getCreatedAt())
                .latitude(monitoringPackage.getLatitude())
                .longitude(monitoringPackage.getLongitude())
                .satellitesNum(monitoringPackage.getSatellitesNum())
                .correctness(monitoringPackage.getCorrectness())
                .correct(monitoringPackage.getCorrect())
                .speed(monitoringPackage.getSpeed())
                .course(monitoringPackage.getCourse())
                .height(monitoringPackage.getHeight())
                .hdop(monitoringPackage.getHdop())
                .build();
    }
}
