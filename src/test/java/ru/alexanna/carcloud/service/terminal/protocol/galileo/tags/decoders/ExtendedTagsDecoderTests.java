package ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.decoders;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.*;
import ru.alexanna.carcloud.dto.MonitoringPackage;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExtendedTagsDecoderTests {

    private ExtendedTagsDecoder sut;
    private static final ByteBuf buf = Unpooled.buffer();
    private List<Double> expectedExtendedTags;

    @BeforeAll
    public static void setBuf() {
        buf.writerIndex(2); //Пропустить первые 2 байта - Длина пакета расширенных тэгов

        buf.writeShortLE(0x83); //MCC tag
        buf.writeShortLE(169);

        buf.writeShortLE(0x81); //CID tag
        buf.writeShortLE(38);

        buf.writeShortLE(0x8f); //GLONASS tag
        buf.writeBytes(new byte[] {0x0a, 0x05, 0x1e, 33});

        buf.writeShortLE(0x86); //Temp sensor 0
        buf.writeShortLE(6); //Temp ID = 6
        buf.writeShortLE(0x801a); //Set temp value

        for (int i = 0x01; i <= 0x80; i++) {
            buf.writeShortLE(i);
            buf.writeIntLE(5);
        }

        buf.setShortLE(0, buf.writerIndex() - 2); // Записать количество байт в буфере
    }
    @BeforeEach
    public void setUp() {
        sut = new ExtendedTagsDecoder(0xfe, new MonitoringPackage());
        expectedExtendedTags = new ArrayList<>();
        for (int i = 0x01; i <= 0x80; i++) {
            expectedExtendedTags.add(0.05);
        }
    }

    @Test
    @DisplayName("Decoding ByteBuf with various data in Extended tags")
    public void decodeByteBufWithExtendedTags() {
        List<Double> actualExtendedTags = sut.decode(buf).getExtendedTags();

        assertEquals(expectedExtendedTags, actualExtendedTags);
    }

    @AfterAll
    static void tearDown() {
        buf.release();
    }
}
