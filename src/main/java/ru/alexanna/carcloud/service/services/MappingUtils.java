package ru.alexanna.carcloud.service.services;

import org.springframework.stereotype.Service;
import ru.alexanna.carcloud.dto.*;
import ru.alexanna.carcloud.entities.Item;
import ru.alexanna.carcloud.entities.TerminalMessage;

import java.util.stream.Collectors;

@Service
public class MappingUtils {
    public TerminalMessage mapToTerminalMessage(MonitoringPackage monitoringPackage, Item item) {
        TerminalMessage terminalMessage = TerminalMessage.builder()
                .imei(monitoringPackage.getRegInfo().getImei())
                .item(item)
                .deviceId(monitoringPackage.getRegInfo().getDeviceId())
                .hardVer(monitoringPackage.getRegInfo().getHardVer())
                .softVer(monitoringPackage.getRegInfo().getSoftVer())
                .recordNum(monitoringPackage.getDeviceInfo().getRecordNum())
                .supplyVol(monitoringPackage.getDeviceInfo().getSupplyVol())
                .batteryVol(monitoringPackage.getDeviceInfo().getBatteryVol())
                .deviceTemp(monitoringPackage.getDeviceInfo().getDeviceTemp())
                .status(monitoringPackage.getDeviceInfo().getStatus())
                .createdAt(monitoringPackage.getCreatedAt())
                .latitude(monitoringPackage.getNavigationInfo().getLatitude())
                .longitude(monitoringPackage.getNavigationInfo().getLongitude())
                .satellitesNum(monitoringPackage.getNavigationInfo().getSatellitesNum())
                .correctness(monitoringPackage.getNavigationInfo().getCorrectness())
                .correct(monitoringPackage.getNavigationInfo().getCorrect())
                .speed(monitoringPackage.getNavigationInfo().getSpeed())
                .course(monitoringPackage.getNavigationInfo().getCourse())
                .height(monitoringPackage.getNavigationInfo().getHeight())
                .hdop(monitoringPackage.getNavigationInfo().getHdop())
                .userTags(monitoringPackage.getUserTags())
                .analogInputs(monitoringPackage.getAnalogInputs())
                .fuelSensors(monitoringPackage.getFuelSensors())
                .tempSensors(monitoringPackage.getTempSensors())
                .can8BitList(monitoringPackage.getCan8BitList())
                .can16BitList(monitoringPackage.getCan16BitList())
                .can32BitList(monitoringPackage.getCan32BitList())
                .extendedTags(monitoringPackage.getExtendedTags())
                .build();
//        item.getTerminalMessages().add(terminalMessage);
        return terminalMessage;
    }
    public MonitoringPackage mapToMonitoringPackage(TerminalMessage tm) {
        return MonitoringPackage.builder()
                .createdAt(tm.getCreatedAt())
                .deviceInfo(new DeviceInfo(tm.getRecordNum(), tm.getSupplyVol(), tm.getBatteryVol(), tm.getDeviceTemp(), tm.getStatus()))
                .regInfo(new RegInfo(tm.getImei(), tm.getDeviceId(), tm.getHardVer(), tm.getSoftVer()))
                .navigationInfo(new NavigationInfo(tm.getLatitude(), tm.getLongitude(), tm.getSatellitesNum(), tm.getCorrectness(), tm.getCorrect(), tm.getSpeed(), tm.getCourse(), tm.getHeight(), tm.getHdop()))
                .analogInputs(tm.getAnalogInputs())
                .fuelSensors(tm.getFuelSensors())
                .userTags(tm.getUserTags())
                .tempSensors(tm.getTempSensors())
                .can8BitList(tm.getCan8BitList())
                .can16BitList(tm.getCan16BitList())
                .can32BitList(tm.getCan32BitList())
                .extendedTags(tm.getExtendedTags())
                .build();
    }

    public ItemDto mapToItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .imei(item.getImei())
                .name(item.getName())
                .phoneNum1(item.getPhoneNum1())
                .phoneNum2(item.getPhoneNum2())
                .deviceType(item.getDeviceType())
                .description(item.getDescription())
                .monitoringPackages(item.getTerminalMessages().stream().map(this::mapToMonitoringPackage).collect(Collectors.toSet()))
                .build();
    }
}
