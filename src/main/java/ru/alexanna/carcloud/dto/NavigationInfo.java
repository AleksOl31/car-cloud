package ru.alexanna.carcloud.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
