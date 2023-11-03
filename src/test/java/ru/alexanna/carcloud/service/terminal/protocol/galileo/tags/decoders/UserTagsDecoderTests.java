package ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.decoders;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.*;
import ru.alexanna.carcloud.dto.MonitoringPackage;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserTagsDecoderTests {

    private ByteBuf buf;

    @BeforeEach
    public void setUp() {
        buf = Unpooled.buffer();
    }

    @Test
    @DisplayName("Decoding ByteBuf with user data array")
    public void decodeUserDataArray() {
        final int numUserBytes = 17;
        buf.writeByte(numUserBytes);
        for (int i = 1; i <= numUserBytes; i++) {
            buf.writeByte(5);
        }
        UserTagsDecoder sut = new UserTagsDecoder(0xea, new MonitoringPackage());

        sut.decode(buf);

        assertEquals(buf.readerIndex(), numUserBytes + 1);
    }

    @Test
    @DisplayName("Decoding ByteBuf with user tag")
    public void decodeUserTag() {
        buf.writeIntLE(5);
        UserTagsDecoder sut = new UserTagsDecoder(0xe5, new MonitoringPackage());
        List<Integer> expectedUserTags = new ArrayList<>();
        expectedUserTags.add(5);

        List<Integer> actualUserTags = sut.decode(buf).getUserTags();

        assertEquals(expectedUserTags, actualUserTags);
    }

    @AfterEach
    public void tearDown() {
        buf.release();
    }
}
