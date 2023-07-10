package ru.alexanna.carcloud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ItemDto {
    private Long id;
    private String imei;
    private String name;
    private String phoneNum1;
    private String phoneNum2;
    private String deviceType;
    private String description;
    private Integer deviceId;
    private Integer hardVer;
    private Integer softVer;
    private Boolean connectionState;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<MonitoringPackage> monitoringPackages;
}
