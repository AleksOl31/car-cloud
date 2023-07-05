package ru.alexanna.carcloud.dto;

import lombok.*;

import java.util.*;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class MonitoringPackage {
    private Date createdAt;
    private RegInfo regInfo = new RegInfo();
    private DeviceInfo deviceInfo = new DeviceInfo();
    private NavigationInfo navigationInfo = new NavigationInfo();
    private List<Integer> userTags = new ArrayList<>();
    private List<Integer> analogInputs = new ArrayList<>();
    private Set<TempSensor> tempSensors = new HashSet<>();
    private Set<FuelSensor> fuelSensors = new HashSet<>();
    private List<Integer> can8BitList = new ArrayList<>();
    private List<Integer> can16BitList = new ArrayList<>();
    private List<Integer> can32BitList = new ArrayList<>();
    private List<Double> extendedTags = new ArrayList<>();

}
