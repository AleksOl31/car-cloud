package ru.alexanna.carcloud.service.terminal.protocol.galileo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class GalileoPackageParserTest {

    @Test
    @Timeout(1)
    void parse() {
        try {
//            TimeUnit.SECONDS.sleep(2);
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(50, 10 * 5, "10 x 5 must be 50");
    }
}