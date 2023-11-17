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
        Double[] tags0 = {5., 5., 5., 6., 6., 6.};
        Double[] tags1 = {6., 6., 6., 0., 7., 7.};
        Double[] tags2 = {7., 7., 7., 8., 9., 9.};
        TerminalMessage terminalMessage0 = TerminalMessage.builder()
                .id(1L)
                .extendedTags(Arrays.asList(tags0))
                .build();
        TerminalMessage terminalMessage1 = TerminalMessage.builder()
                .id(2L)
                .extendedTags(Arrays.asList(tags1))
                .build();
        TerminalMessage terminalMessage2 = TerminalMessage.builder()
                .id(3L)
                .extendedTags(Arrays.asList(tags2))
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
