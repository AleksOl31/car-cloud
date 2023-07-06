package ru.alexanna.carcloud.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.*;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class MonitoringPackage {
//    @JsonProperty("crt")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date createdAt;
//    @JsonProperty("rInf")
    @JsonIgnore
    private RegInfo regInfo = new RegInfo();
//    @JsonProperty("dInf")
    private DeviceInfo deviceInfo = new DeviceInfo();
//    @JsonProperty("nav")
    private NavigationInfo navigationInfo = new NavigationInfo();
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Integer> userTags = new ArrayList<>();
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Integer> analogInputs = new ArrayList<>();
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<TempSensor> tempSensors = new HashSet<>();
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<FuelSensor> fuelSensors = new HashSet<>();
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Integer> can8BitList = new ArrayList<>();
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Integer> can16BitList = new ArrayList<>();
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Integer> can32BitList = new ArrayList<>();
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonProperty("exTgs")
    private List<Double> extendedTags = new ArrayList<>();

}
