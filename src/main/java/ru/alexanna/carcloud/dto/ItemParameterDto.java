package ru.alexanna.carcloud.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class ItemParameterDto {
    private Long id;
    private Integer index;
    private String type;
    private String name;
}
