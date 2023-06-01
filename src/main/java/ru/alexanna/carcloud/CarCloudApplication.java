package ru.alexanna.carcloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;
import ru.alexanna.carcloud.service.terminal.server.BaseNettyServer;

@Slf4j
@SpringBootApplication
@ConfigurationPropertiesScan
public class CarCloudApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(CarCloudApplication.class, args);
        BaseNettyServer nettyServer = context.getBean(BaseNettyServer.class);
        nettyServer.run();
    }

}
