package ru.alexanna.carcloud.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Embeddable
public class ParameterName {
    @Column(nullable = false)
    private Integer index;
    @Column(nullable = false)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParameterName that = (ParameterName) o;

        if (!index.equals(that.index)) return false;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = index.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
