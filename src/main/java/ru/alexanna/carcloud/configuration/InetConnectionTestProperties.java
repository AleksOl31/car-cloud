package ru.alexanna.carcloud.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@ConfigurationProperties(prefix = "inet.testing")
@PropertySource("classpath:connection.properties")
@Getter
@Setter
public class InetConnectionTestProperties {
    private String hostname1;
    private String hostname2;
    private int testPort;
    private int testTimeout;
    private long testPeriod;
}
