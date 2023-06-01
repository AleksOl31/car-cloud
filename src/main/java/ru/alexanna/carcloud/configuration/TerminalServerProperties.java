package ru.alexanna.carcloud.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "terminal.server")
@Getter
@Setter
public class TerminalServerProperties {
    private int readTimeout;
    private int galileoPort;
    private int scoutPort;
}
