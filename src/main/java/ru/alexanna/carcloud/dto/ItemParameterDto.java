package ru.alexanna.carcloud.dto;

import lombok.*;
import ru.alexanna.carcloud.entities.ItemParameter;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class ItemParameterDto {
    private Integer index;
    private ItemParameter.Type type;
    private String name;
}
