package ru.alexanna.carcloud.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DeviceInfo {
    private Integer recordNum;
    private Integer supplyVol;
    private Integer batteryVol;
    private Integer deviceTemp;
    private Integer status;
}
