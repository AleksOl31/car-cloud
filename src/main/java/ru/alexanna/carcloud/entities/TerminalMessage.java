package ru.alexanna.carcloud.entities;

import lombok.*;
import ru.alexanna.carcloud.dto.FuelSensor;
import ru.alexanna.carcloud.dto.TempSensor;

import javax.persistence.*;
import java.util.*;


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
    @Column(nullable = false, updatable = false)
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

    @ElementCollection
    @CollectionTable(name = "temp_sensors")
    private Set<TempSensor> tempSensors = new HashSet<>();
    @ElementCollection
    @CollectionTable(name = "fuel_sensors")
    @AttributeOverride(name = "address", column = @Column(name = "id", nullable = false))
    private Set<FuelSensor> fuelSensors = new HashSet<>();
    @ElementCollection
    @CollectionTable(name = "user_tags")
    @OrderColumn(name = "tags_order")
    @Column(name = "tag_value")
    private List<Integer> userTags = new ArrayList<>();
    @ElementCollection
    @CollectionTable(name = "analog_inputs")
    @OrderColumn(name = "inputs_order")
    @Column(name = "input_value")
    private List<Integer> analogInputs = new ArrayList<>();
    @ElementCollection
    @CollectionTable(name = "can_one_byte_values")
    @OrderColumn(name = "values_order")
    @Column(name = "tag_value")
    private List<Integer> can8BitList = new ArrayList<>();
    @ElementCollection
    @CollectionTable(name = "can_two_bytes_values")
    @OrderColumn(name = "values_order")
    @Column(name = "tag_value")
    private List<Integer> can16BitList = new ArrayList<>();
    @ElementCollection
    @CollectionTable(name = "can_four_bytes_values")
    @OrderColumn(name = "values_order")
    @Column(name = "tag_value")
    private List<Integer> can32BitList = new ArrayList<>();
    @ElementCollection
    @CollectionTable(name = "extended_tags")
    @OrderColumn(name = "tags_order")
    @Column(name = "tag_value")
    private List<Double> extendedTags = new ArrayList<>();

}
