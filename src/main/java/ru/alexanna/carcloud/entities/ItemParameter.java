package ru.alexanna.carcloud.entities;

import lombok.*;
import ru.alexanna.carcloud.dto.ParameterName;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor
//@Data
@Getter
@Setter
@Builder

@Entity
@Table(name = "parameters")
public class ItemParameter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;
    @ElementCollection
    @CollectionTable(name = "param_names", joinColumns = @JoinColumn(name = "parameter_id"))
    @AttributeOverride(name = "index", column = @Column(name = "num", nullable = false))
//    @org.hibernate.annotations.OrderBy(clause = "num")
    private Set<ParameterName> names = new HashSet<>();
    @Enumerated(EnumType.STRING)
    private Type type;

    public enum Type {
        FUEL("fuelSensors"), TEMP("tempSensors"), ANALOG_INPUT("analogInputs"), USER("userTags"), CAN_ONE_BYTE("can8BitList"),
        CAN_TWO_BYTE("can16BitList"), CAN_FOUR_BYTE("can32BitList"), EXTENDED_TAGS("extendedTags");
        private final String name;

        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
        public static Type fromName(String name) {
            for (final Type type: values()) {
                if (type.getName().equalsIgnoreCase(name))
                    return type;
            }
            throw new IllegalStateException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemParameter that = (ItemParameter) o;

        if (!item.equals(that.item)) return false;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        int result = item.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }
}
