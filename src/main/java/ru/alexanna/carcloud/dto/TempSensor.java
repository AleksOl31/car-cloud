package ru.alexanna.carcloud.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Embeddable
public class TempSensor {
    @Column(nullable = false)
    private Integer id;
    @Column(nullable = false)
    private Integer temperature;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TempSensor that = (TempSensor) o;

        if (!id.equals(that.id)) return false;
        return temperature.equals(that.temperature);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + temperature.hashCode();
        return result;
    }
}
