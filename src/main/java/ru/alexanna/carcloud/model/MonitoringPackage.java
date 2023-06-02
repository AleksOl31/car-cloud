package ru.alexanna.carcloud.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class MonitoringPackage {
    private Device device;
    private Set<MonitoringData> monitoringDataSet = new HashSet<>();

    public void add(MonitoringData monitoringData) {
        monitoringDataSet.add(monitoringData);
    }
}
