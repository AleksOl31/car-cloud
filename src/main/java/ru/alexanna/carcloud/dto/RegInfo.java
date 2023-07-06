package ru.alexanna.carcloud.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegInfo {
    private String imei;
//    @JsonProperty("dId")
    private Integer deviceId;
//    @JsonProperty("hVer")
    private Integer hardVer;
//    @JsonProperty("sVer")
    private Integer softVer;
}
