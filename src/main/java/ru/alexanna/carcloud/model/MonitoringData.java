package ru.alexanna.carcloud.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MonitoringData {
    private Navigation navigation = new Navigation();
    private List<Integer> userTags = new ArrayList<>();
}
