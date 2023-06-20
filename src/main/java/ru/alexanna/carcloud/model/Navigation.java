package ru.alexanna.carcloud.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.Objects;

@Setter
@Getter
@ToString
public class Navigation {

    private Date createdAt;
    private Location location;
    private Double speed;
    private Double course;
    private Integer height;
    private Double hdop;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Navigation that = (Navigation) o;
        return Objects.equals(createdAt, that.createdAt) && Objects.equals(location, that.location) && Objects.equals(speed, that.speed) && Objects.equals(course, that.course) && Objects.equals(height, that.height) && Objects.equals(hdop, that.hdop);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdAt, location, speed, course, height, hdop);
    }
}
