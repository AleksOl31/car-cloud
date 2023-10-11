package ru.alexanna.carcloud.entities;

import lombok.*;
import ru.alexanna.carcloud.dto.RegInfo;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String imei;
    private String name;
    @Column(name = "phone_num1")
    private String phoneNum1;
    @Column(name = "phone_num2")
    private String phoneNum2;
    private String deviceType;
    private Integer deviceId;
    private Integer hardVer;
    private Integer softVer;
    @Column(name = "con_state", nullable = false)
    private Boolean connectionState;
    private String description;
    private String remoteAddress;
    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private Set<TerminalMessage> terminalMessages = new HashSet<>();
    @OneToMany(mappedBy = "item", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE}, orphanRemoval = true)
    private Set<ItemParameter> parameters = new HashSet<>();

    public void setConnected (String remoteAddress) {
        connectionState = true;
        this.remoteAddress = remoteAddress;
    }

    public void setDisconnected() {
        connectionState = false;
        this.remoteAddress = null;
    }

    public void setRegInfo(RegInfo regInfo) {
        deviceId = regInfo.getDeviceId();
        hardVer = regInfo.getHardVer();
        softVer = regInfo.getSoftVer();
    }
}
