package ru.alexanna.carcloud.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.*;


@NoArgsConstructor
@Getter
@Setter
@ToString
public class MonitoringPackage {
    private Date createdAt;
    private RegInfo regInfo = new RegInfo();
    private DeviceInfo deviceInfo = new DeviceInfo();
    private NavigationInfo navigationInfo = new NavigationInfo();
    private final List<Integer> userTags = new ArrayList<>();
    private final List<Integer> analogInputs = new ArrayList<>();
    private final Set<TempSensor> tempSensors = new HashSet<>();
    private final Set<FuelSensor> fuelSensors = new HashSet<>();
    private final List<Integer> can8BitList = new ArrayList<>();
    private final List<Integer> can16BitList = new ArrayList<>();
    private final List<Integer> can32BitList = new ArrayList<>();
    private final List<Double> extendedTags = new ArrayList<>();

}
