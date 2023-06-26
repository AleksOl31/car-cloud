package ru.alexanna.carcloud.entities;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "terminal_messages")
public class TerminalMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String imei;
    private Integer deviceId;
    private Integer hardVer;
    private Integer softVer;
    private Integer recordNum;
    private Integer supplyVol;
    private Integer batteryVol;
    private Integer deviceTemp;
    private Integer status;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date createdAt;
    private Double latitude;
    private Double longitude;
    private Integer satellitesNum;
    private Integer correctness;
    private Boolean correct;
    private Double speed;
    private Double course;
    private Integer height;
    private Double hdop;
 /*   private final List<Integer> userTags = new ArrayList<>();
    private final List<Integer> analogInputs = new ArrayList<>();
    private final List<TempSensor> tempSensors = new ArrayList<>();
    private final List<FuelSensor> fuelSensors = new ArrayList<>();
    private final List<Integer> can8BitList = new ArrayList<>();
    private final List<Integer> can16BitList = new ArrayList<>();
    private final List<Integer> can32BitList = new ArrayList<>();
    private final List<Double> extendedTags = new ArrayList<>();*/

}
