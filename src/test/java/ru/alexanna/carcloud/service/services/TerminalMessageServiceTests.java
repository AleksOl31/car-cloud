package ru.alexanna.carcloud.service.services;

import org.junit.jupiter.api.*;
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
    private List<TerminalMessage> terminalMessages;


    private void setTerminalMessages() {
        terminalMessages = new ArrayList<>();
        terminalMessages.add(TerminalMessage.builder()
                .id(0L)
                .extendedTags(Arrays.asList(5., 5., 5., 0., 6., 6.))
                .build());
        terminalMessages.add(TerminalMessage.builder()
                .id(1L)
                .extendedTags(Arrays.asList(5., 5., 5., 8., 6., 6.))
                .build());
        terminalMessages.add(TerminalMessage.builder()
                .id(2L)
                .extendedTags(Arrays.asList(6., 6., 6., 0., 7., 7.))
                .build());
        terminalMessages.add(TerminalMessage.builder()
                .id(3L)
                .extendedTags(Arrays.asList(7., 7., 7., 6., 9., 9.))
                .build());
        terminalMessages.add(TerminalMessage.builder()
                .id(4L)
                .extendedTags(Arrays.asList(5., 5., 5., 0., 6., 6.))
                .build());
    }

    private void setTerminalMessagesWithNull() {
        terminalMessages = new ArrayList<>();
        terminalMessages.add(TerminalMessage.builder()
                .id(0L)
                .extendedTags(Arrays.asList( 35.33, 36.37, 0.0, 41.88, null, 34.11, 40.57, 3.2))
                .build());
        terminalMessages.add(TerminalMessage.builder()
                .id(1L)
                .extendedTags(Arrays.asList(35.33, 36.37, 0.0, 41.9, null, 34.12, 41.07, 3.2))
                .build());
        terminalMessages.add(TerminalMessage.builder()
                .id(2L)
                .extendedTags(Arrays.asList(35.32, 36.37, 0.0, null /*41.88*/, null, 34.12, 41.44, 3.21))
                .build());
        terminalMessages.add(TerminalMessage.builder()
                .id(3L)
                .extendedTags(Arrays.asList(35.32, 36.37, 0.0, 0.0 /*41.9*/, null, 34.11, 40.81, 3.21))
                .build());
        terminalMessages.add(TerminalMessage.builder()
                .id(4L)
                .extendedTags(Arrays.asList(35.31, 36.37, 0.0, 41.88, null, 34.12, 40.27, 3.2))
                .build());
    }

    private void setOneTerminalMessage() {
        terminalMessages = new ArrayList<>();
        terminalMessages.add(TerminalMessage.builder()
                .id(0L)
                .extendedTags(Arrays.asList(5., 5., 5., 0., 6., 6.))
                .build());
    }

    private void setTwoTerminalMessage() {
        terminalMessages = new ArrayList<>();
        terminalMessages.add(TerminalMessage.builder()
                .id(0L)
                .extendedTags(Arrays.asList(5., 5., 5., 0., 6., 6.))
                .build());
        terminalMessages.add(TerminalMessage.builder()
                .id(1L)
                .extendedTags(Arrays.asList(5., 5., 5., 4., 6., 6.))
                .build());
    }

    @Test
    @DisplayName("Filtering data with NULL")
    public void filteringDataWithNullValue() {
        setTerminalMessagesWithNull();
        List<TerminalMessage> filteredTerminalMessages = sut.filterOutNullValues(terminalMessages, 2);

        List<Double> expectedList = Arrays.asList(41.88, 41.9, null/*41.88*/, 0.0 /*41.9*/, 41.88);
        List<Double> actualList = getResultDataList(filteredTerminalMessages);

        Assertions.assertEquals(expectedList, actualList);
    }

    @Test
    @DisplayName("Filtering values equal zero inside the list")
    public void filteringZeroValuesInsideTheList() {
        setTerminalMessages();
        List<TerminalMessage> filteredTerminalMessages = sut.filterOutNullValues(terminalMessages, 2);

        List<Double> expectedList = Arrays.asList(8.,8.,7.,6.,6.);
        List<Double> actualList = getResultDataList(filteredTerminalMessages);

        Assertions.assertEquals(expectedList, actualList);
    }

    @Test
    @DisplayName("Filtering one value equal zero")
    public void filteringOneZeroValue() {
        setOneTerminalMessage();
        List<TerminalMessage> filteringTerminalMessages = sut.filterOutNullValues(terminalMessages, 2);

        List<Double> expectedList = Arrays.asList(0.);
        List<Double> actualList = getResultDataList(filteringTerminalMessages);

        Assertions.assertEquals(expectedList, actualList);
    }

    @Test
    @DisplayName("Filtering two values list")
    public void filteringTwoValueList() {
        setTwoTerminalMessage();
        List<TerminalMessage> filteredTerminalMessages = sut.filterOutNullValues(terminalMessages, 2);

        List<Double> expectedList = Arrays.asList(4.,4.);
        List<Double> actualList = getResultDataList(filteredTerminalMessages);

        Assertions.assertEquals(expectedList, actualList);
    }

    private List<Double> getResultDataList(List<TerminalMessage> filteringTerminalMessages) {
        ArrayList<Double> resultList = new ArrayList<>();
        filteringTerminalMessages.forEach(terminalMessage ->
                resultList.add(terminalMessage.getExtendedTags().get(3)));
        return resultList;
    }
}
