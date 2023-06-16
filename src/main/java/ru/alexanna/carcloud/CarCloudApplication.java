package ru.alexanna.carcloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@Slf4j
@SpringBootApplication
@ConfigurationPropertiesScan
public class CarCloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarCloudApplication.class, args);
    }

}
