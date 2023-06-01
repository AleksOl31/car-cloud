package ru.alexanna.carcloud.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Device {
    private Integer hardVer;
    private Short softVer;
    private String imei;
    private Integer id;
    private Integer recordNum;
    private Integer supplyVol;
    private Integer batteryVol;
    private Integer temp;

}
