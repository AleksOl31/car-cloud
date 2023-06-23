package ru.alexanna.carcloud.dto;

import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@ToString
public class FuelSensor {
    private final Integer fuelLevel;
    private Integer fuelTemp;
}
