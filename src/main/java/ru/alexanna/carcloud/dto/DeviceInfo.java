package ru.alexanna.carcloud.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DeviceInfo {
//    @JsonProperty("recN")
    private Integer recordNum;
//    @JsonProperty("sVol")
    private Integer supplyVol;
//    @JsonProperty("bVol")
    private Integer batteryVol;
//    @JsonProperty("temp")
    private Integer deviceTemp;
//    @JsonProperty("sts")
    private Integer status;
}
