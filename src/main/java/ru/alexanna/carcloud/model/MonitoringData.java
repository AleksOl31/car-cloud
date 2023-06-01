package ru.alexanna.carcloud.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MonitoringData {
    private Device device;
    private Navigation navigation;
}
