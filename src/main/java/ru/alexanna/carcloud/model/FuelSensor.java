package ru.alexanna.carcloud.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class FuelSensor {
    private Integer fuelLevel;
    private Integer fuelTemp;
}
