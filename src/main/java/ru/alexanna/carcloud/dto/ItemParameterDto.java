package ru.alexanna.carcloud.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ItemParameterDto {
    private Long id;
    private String type;
    private List<ParameterName> names ;

}
