package ru.alexanna.carcloud.service.services;

import org.springframework.stereotype.Service;
import ru.alexanna.carcloud.dto.*;
import ru.alexanna.carcloud.entities.Item;
import ru.alexanna.carcloud.entities.ItemParameter;
import ru.alexanna.carcloud.entities.TerminalMessage;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.stream.Collectors;


@Service
public class MappingUtils {
    public TerminalMessage mapToTerminalMessage(MonitoringPackage monitoringPackage, Item item) {
        return TerminalMessage.builder()
                .item(item)
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

    public MonitoringPackage mapToMonitoringPackage(TerminalMessage tm) {
        return MonitoringPackage.builder()
                .createdAt(tm.getCreatedAt())
                .deviceInfo(new DeviceInfo(tm.getRecordNum(), tm.getSupplyVol(), tm.getBatteryVol(), tm.getDeviceTemp(), tm.getStatus()))
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
        ItemDto itemDto = ItemDto.builder()
                .id(item.getId())
                .imei(item.getImei())
                .name(item.getName())
                .phoneNum1(item.getPhoneNum1())
                .phoneNum2(item.getPhoneNum2())
                .deviceType(item.getDeviceType())
                .deviceId(item.getDeviceId())
                .hardVer(item.getHardVer())
                .softVer(item.getSoftVer())
                .connectionState(item.getConnectionState())
                .description(item.getDescription())
                .build();
        if (item.getParameters() != null)
            itemDto.setParameters(item.getParameters().stream().map(this::mapToItemParameterDto).collect(Collectors.toSet()));
        else
            itemDto.setParameters(Collections.emptySet());
        return itemDto;
    }

    public Item mapToItem(ItemDto itemDto) {
        Item item = Item.builder()
                .imei(itemDto.getImei())
                .name(itemDto.getName())
                .phoneNum1(itemDto.getPhoneNum1())
                .phoneNum2(itemDto.getPhoneNum2())
                .deviceType(itemDto.getDeviceType())
                .description(itemDto.getDescription())
                .connectionState(itemDto.getConnectionState())
                .build();
        item.setParameters(itemDto.getParameters().stream().map(itemParameterDto ->
            mapToItemParameter(itemParameterDto, item)).collect(Collectors.toSet()));
        System.out.println(item);
        return item;
    }

    public ItemParameterDto mapToItemParameterDto(ItemParameter itemParameter) {
        return ItemParameterDto.builder()
                .id(itemParameter.getId())
                .type(itemParameter.getType().getName())
                .names(itemParameter.getNames().stream()
                        .sorted(Comparator.comparingInt(ParameterName::getIndex))
                        .collect(Collectors.toList()))
                .build();
    }

    public ItemParameter mapToItemParameter(ItemParameterDto itemParameterDto, Item item) {
        return ItemParameter.builder()
                .item(item)
                .type(ItemParameter.Type.fromName(itemParameterDto.getType()))
                .names(new HashSet<>(itemParameterDto.getNames()))
                .build();
    }

}
