package ru.alexanna.carcloud.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class TempSensor {
    private Integer id;
    private Integer temp;
}
