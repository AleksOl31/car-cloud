package ru.alexanna.carcloud.service.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.alexanna.carcloud.entities.TerminalMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class TerminalMessageServiceTests {
    @Autowired
    private TerminalMessageService sut;
    private static final List<TerminalMessage> terminalMessages = new ArrayList<>();

    @BeforeAll
    public static void setTerminalMessages() {
        TerminalMessage terminalMessage0 = TerminalMessage.builder()
                .id(1L)
                .extendedTags(Arrays.asList(5., 5., 5., 6., 6., 6.))
                .build();
        TerminalMessage terminalMessage1 = TerminalMessage.builder()
                .id(2L)
                .extendedTags(Arrays.asList(6., 6., 6., 0., 7., 7.))
                .build();
        TerminalMessage terminalMessage2 = TerminalMessage.builder()
                .id(3L)
                .extendedTags(Arrays.asList(7., 7., 7., 8., 9., 9.))
                .build();
        terminalMessages.add(terminalMessage0);
        terminalMessages.add(terminalMessage1);
        terminalMessages.add(terminalMessage2);
    }

    @Test
    @DisplayName("Filtering values equal zero")
    public void filteringZeroValues() {
        List<TerminalMessage> filteringTerminalMessages = sut.filterOutNullValues(terminalMessages);

        Assertions.assertEquals(7.0, filteringTerminalMessages.get(1).getExtendedTags().get(3));
    }
}
