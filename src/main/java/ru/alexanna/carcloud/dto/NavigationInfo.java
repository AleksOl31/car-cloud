package ru.alexanna.carcloud.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NavigationInfo {
//    @JsonProperty("lat")
    private Double latitude;
//    @JsonProperty("lon")
    private Double longitude;
//    @JsonProperty("satN")
    private Integer satellitesNum;
//    @JsonProperty("crctn")
    private Integer correctness;
//    @JsonProperty("cor")
    private Boolean correct;
    private Double speed;
//    @JsonProperty("crs")
    private Double course;
    private Integer height;
    private Double hdop;
}
