package ru.alexanna.carcloud.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Device {
    private Integer recordNum;
    private Integer supplyVol;
    private Integer batteryVol;
    private Integer temperature;
    private Integer status;
}
