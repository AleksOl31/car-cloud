package ru.alexanna.carcloud.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ItemParameterDto {
    private Long id;
    private String type;
    private Set<ParameterName> names ;

}
