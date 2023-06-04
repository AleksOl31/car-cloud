package ru.alexanna.carcloud.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RegInfo {
    private String imei;
    private Integer id;
    private Integer hardVer;
    private Integer softVer;
}
