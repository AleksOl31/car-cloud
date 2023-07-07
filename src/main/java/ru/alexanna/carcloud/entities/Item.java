package ru.alexanna.carcloud.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
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
    @Column(name = "con_state")
    private Boolean connectionState;
    private String description;
    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private Set<TerminalMessage> terminalMessages = new HashSet<>();

/*    public void add(TerminalMessage terminalMessage) {
        terminalMessages.add(terminalMessage);
    }*/
}
