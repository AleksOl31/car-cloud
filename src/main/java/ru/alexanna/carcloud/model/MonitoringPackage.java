package ru.alexanna.carcloud.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class MonitoringPackage {
    private final RegInfo regInfo = new RegInfo();
    private final Device device = new Device();
    private Set<MonitoringData> monitoringDataSet = new HashSet<>();

    public void add(MonitoringData monitoringData) {
        monitoringDataSet.add(monitoringData);
    }
}
