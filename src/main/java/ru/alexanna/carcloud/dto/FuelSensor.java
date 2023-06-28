package ru.alexanna.carcloud.dto;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Embeddable
public class FuelSensor {
    @Column(nullable = false)
    private Integer address;
    @Column(nullable = false)
    private Integer fuelLevel;
    @Column(nullable = false)
    private Integer fuelTemp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FuelSensor that = (FuelSensor) o;

        if (!address.equals(that.address)) return false;
        if (!fuelLevel.equals(that.fuelLevel)) return false;
        return Objects.equals(fuelTemp, that.fuelTemp);
    }

    @Override
    public int hashCode() {
        int result = address.hashCode();
        result = 31 * result + fuelLevel.hashCode();
        result = 31 * result + (fuelTemp != null ? fuelTemp.hashCode() : 0);
        return result;
    }
}
