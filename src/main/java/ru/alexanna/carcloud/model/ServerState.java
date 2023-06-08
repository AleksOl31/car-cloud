package ru.alexanna.carcloud.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ServerState {
    private boolean running;
    private String stateStr;
    private String actionStr;
}
