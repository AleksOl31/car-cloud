package ru.alexanna.carcloud.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegInfo {
    private String imei;
    private Integer deviceId;
    private Integer hardVer;
    private Integer softVer;
}
