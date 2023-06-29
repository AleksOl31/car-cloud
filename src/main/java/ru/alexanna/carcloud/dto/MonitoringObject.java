package ru.alexanna.carcloud.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MonitoringObject {
    private Long id;
    private String imei;
    private String name;
    private String phoneNum;
}
