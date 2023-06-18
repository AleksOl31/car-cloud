package ru.alexanna.carcloud.model;

import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@ToString
public class FuelSensor {
    private final Integer fuelLevel;
    private Integer fuelTemp;
}
