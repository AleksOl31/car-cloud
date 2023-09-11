package ru.alexanna.carcloud.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "inet.testing")
@Getter
@Setter
public class InetConnectionTestProperties {
    private String hostname1;
    private String hostname2;
    private int testTimeout;
    private long testPeriod;
}
