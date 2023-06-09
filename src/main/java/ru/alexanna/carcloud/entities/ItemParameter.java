package ru.alexanna.carcloud.entities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor
@Data
@Entity
@Table(name = "parameters")
public class ItemParameter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;
    @Column(nullable = false)
    private Integer index;
    @Column(nullable = false)
    private String name;
    private Type type;

    public enum Type {
        FUEL, TEMP, ANALOG_INPUT, USER, CAN_ONE_BYTE, CAN_TWO_BYTE, CAN_FOUR_BYTE, EXTENDED_TAGS
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemParameter that = (ItemParameter) o;

        if (!item.equals(that.item)) return false;
        if (!name.equals(that.name)) return false;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        int result = item.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }
}
