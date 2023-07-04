package ru.alexanna.carcloud.dto;

import lombok.*;
import ru.alexanna.carcloud.entities.TerminalMessage;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ItemDto {
    private Long id;
    private String imei;
    private String name;
    private String phoneNum1;
    private String phoneNum2;
    private String deviceType;
    private String description;
    private Set<TerminalMessage> terminalMessages;
}
