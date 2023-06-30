package ru.alexanna.carcloud.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Item {
    private Long id;
    private String imei;
    private String name;
    private String phoneNum1;
    private String phoneNum2;
    private String deviceType;
    private String description;
}
