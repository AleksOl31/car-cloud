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
    private Integer temp;
    private Integer status;
}
