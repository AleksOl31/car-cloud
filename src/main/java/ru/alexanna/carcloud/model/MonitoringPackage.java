package ru.alexanna.carcloud.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@NoArgsConstructor
@Getter
@Setter
@ToString
public class MonitoringPackage {
        private RegInfo regInfo = new RegInfo();
//    private String imei;
//    private Integer deviceId;
//    private Integer hardVer;
//    private Integer softVer;
    //    private final Device device = new Device();
    private Integer recordNum;
    private Integer supplyVol;
    private Integer batteryVol;
    private Integer deviceTemp;
    private Integer status;
    //    private final Navigation navigation = new Navigation();
    private Date createdAt;
    //    private Location location;
    private Double latitude;
    private Double longitude;
    private Integer satellitesNum;
    private Integer correctness;
    private Boolean correct;
    private Double speed;
    private Double course;
    private Integer height;
    private Double hdop;
    private final List<Integer> userTags = new ArrayList<>();
    private final List<Integer> analogInputs = new ArrayList<>();
    private final List<TempSensor> tempSensors = new ArrayList<>();
    private final List<FuelSensor> fuelSensors = new ArrayList<>();
    private final List<Integer> can8BitList = new ArrayList<>();
    private final List<Integer> can16BitList = new ArrayList<>();
    private final List<Integer> can32BitList = new ArrayList<>();
    private final List<Double> extendedTags = new ArrayList<>();

//    @Override
//    public String toString() {
//        return "MonitoringPackage{" +
//                "regInfo=" + regInfo + "," + "\r\n" +
//                "device=" + device + "," + "\r\n" +
//                "navigation=" + navigation + "," + "\r\n" +
//                "userTags=" + userTags + ", " + "analogInputs=" + analogInputs + ", " +
//                "tempSensors=" + tempSensors + ", " + "fuelSensors=" + fuelSensors + "," + "\r\n" +
//                "can8Bit=" + can8BitList + ", " + "can16Bit=" + can16BitList + ", " + "can32Bit=" + can32BitList + "," + "\r\n" +
//                "extTags=" + extendedTags + "," + "\r\n" +
//                '}' + "\r\n";
//    }
}
