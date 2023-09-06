package ru.alexanna.carcloud.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ItemParameterDto {

    @JsonIgnore
    private Long id;
    private String type;
    private List<ParameterName> names ;

}
