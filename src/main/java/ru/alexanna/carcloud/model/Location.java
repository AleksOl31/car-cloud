package ru.alexanna.carcloud.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class Location {
    private Double latitude;
    private Double longitude;
    private Integer satellitesNum;
    private Integer correctness;
    private Boolean correct;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(latitude, location.latitude) && Objects.equals(longitude, location.longitude) /*&& Objects.equals(satellitesNum, location.satellitesNum) && Objects.equals(correctness, location.correctness) && Objects.equals(correct, location.correct)*/;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude/*, satellitesNum, correctness, correct*/);
    }

}
