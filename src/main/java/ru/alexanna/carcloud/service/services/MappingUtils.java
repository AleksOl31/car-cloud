package ru.alexanna.carcloud.service.services;

import org.springframework.stereotype.Service;
import ru.alexanna.carcloud.dto.ItemDto;
import ru.alexanna.carcloud.entities.Item;
import ru.alexanna.carcloud.entities.TerminalMessage;
import ru.alexanna.carcloud.dto.MonitoringPackage;

@Service
public class MappingUtils {
    public TerminalMessage mapToTerminalMessage(MonitoringPackage monitoringPackage, Item item) {
        return TerminalMessage.builder()
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
                .terminalMessages(item.getTerminalMessages())
                .build();
    }
}
