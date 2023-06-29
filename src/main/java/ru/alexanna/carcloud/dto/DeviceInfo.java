package ru.alexanna.carcloud.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DeviceInfo {
    private Integer recordNum;
    private Integer supplyVol;
    private Integer batteryVol;
    private Integer deviceTemp;
    private Integer status;
}
