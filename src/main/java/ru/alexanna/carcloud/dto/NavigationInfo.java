package ru.alexanna.carcloud.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class NavigationInfo {
    private Double latitude;
    private Double longitude;
    private Integer satellitesNum;
    private Integer correctness;
    private Boolean correct;
    private Double speed;
    private Double course;
    private Integer height;
    private Double hdop;
}
