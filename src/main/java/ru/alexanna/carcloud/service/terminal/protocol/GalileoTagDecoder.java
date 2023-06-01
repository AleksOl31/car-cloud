package ru.alexanna.carcloud.service.terminal.protocol;

import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;
import ru.alexanna.carcloud.model.Location;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

import static java.util.Map.entry;

@Component
public class GalileoTagDecoder {
    private static final Map<Integer, Integer> tagLengthMap;

    static {
        tagLengthMap = Map.ofEntries(
                entry(0x01, 1),
                entry(0x02, 1),
                entry(0x03, 15),
                entry(0x04, 2),
                entry(0x10, 2),
                entry(0x20, 4),
                entry(0x30, 9),
                entry(0x33, 4),
                entry(0x34, 2),
                entry(0x35, 1),
                entry(0x40, 2),
                entry(0x41, 2),
                entry(0x42, 2),
                entry(0x43, 1),
                entry(0x44, 4),
                entry(0x45, 2),
                entry(0x46, 2),
                entry(0x48, 2),
                entry(0x50, 2),
                entry(0x51, 2),
                entry(0x52, 2),
                entry(0x53, 2),
                entry(0x58, 2),
                entry(0x59, 2),
                entry(0x70, 2),
                entry(0x71, 2),
                entry(0x72, 2),
                entry(0x73, 2),
                entry(0x74, 2),
                entry(0x75, 2),
                entry(0x76, 2),
                entry(0x77, 2),
                entry(0x90, 4),
                entry(0xC0, 4),
                entry(0xC1, 4),
                entry(0xC2, 4),
                entry(0xC3, 4),
                entry(0xC4, 1),
                entry(0xC5, 1),
                entry(0xC6, 1),
                entry(0xC7, 1),
                entry(0xC8, 1),
                entry(0xC9, 1),
                entry(0xCA, 1),
                entry(0xCB, 1),
                entry(0xCC, 1),
                entry(0xCD, 1),
                entry(0xCE, 1),
                entry(0xCF, 1),
                entry(0xD0, 1),
                entry(0xD1, 1),
                entry(0xD2, 1),
                entry(0xD3, 4),
                entry(0xD4, 4),
                entry(0xD5, 1),
                entry(0xD6, 2),
                entry(0xD7, 2),
                entry(0xD8, 2),
                entry(0xD9, 2),
                entry(0xDA, 2),
                entry(0xDB, 4),
                entry(0xDC, 4),
                entry(0xDD, 4),
                entry(0xDE, 4),
                entry(0xDF, 4),
                entry(0x54, 2),
                entry(0x55, 2),
                entry(0x56, 2),
                entry(0x57, 2),
                entry(0x80, 3),
                entry(0x81, 3),
                entry(0x82, 3),
                entry(0x83, 3),
                entry(0x84, 3),
                entry(0x85, 3),
                entry(0x86, 3),
                entry(0x87, 3),
                entry(0x60, 2),
                entry(0x61, 2),
                entry(0x62, 2),
                entry(0x63, 3),
                entry(0x64, 3),
                entry(0x65, 3),
                entry(0x66, 3),
                entry(0x67, 3),
                entry(0x68, 3),
                entry(0x69, 3),
                entry(0x6A, 3),
                entry(0x6B, 3),
                entry(0x6C, 3),
                entry(0x6D, 3),
                entry(0x6E, 3),
                entry(0x6F, 3),
                entry(0x88, 1),
                entry(0x89, 1),
                entry(0x8A, 1),
                entry(0x8B, 1),
                entry(0x8C, 1),
                entry(0x78, 2),
                entry(0x79, 2),
                entry(0xA0, 1),
                entry(0xA1, 1),
                entry(0xA2, 1),
                entry(0xA3, 1),
                entry(0xA4, 1),
                entry(0xA5, 1),
                entry(0xA6, 1),
                entry(0xA7, 1),
                entry(0xA8, 1),
                entry(0xA9, 1),
                entry(0xAA, 1),
                entry(0xAB, 1),
                entry(0xAC, 1),
                entry(0xAD, 1),
                entry(0xAE, 1),
                entry(0xAF, 1),
                entry(0xB0, 2),
                entry(0xB1, 2),
                entry(0xB2, 2),
                entry(0xB3, 2),
                entry(0xB4, 2),
                entry(0xB5, 2),
                entry(0xB6, 2),
                entry(0xB7, 2),
                entry(0xB8, 2),
                entry(0xB9, 2),
                entry(0xF0, 4),
                entry(0xF1, 4),
                entry(0xF2, 4),
                entry(0xF3, 4),
                entry(0xF4, 4),
                entry(0xF5, 4),
                entry(0xF6, 4),
                entry(0xF7, 4),
                entry(0xF8, 4),
                entry(0xF9, 4),
                entry(0x5A, 4),
                // Здесь 0x5B (см. описание протокола)
                entry(0x47, 4),
                entry(0x5C, 68),
                entry(0x5D, 3),
                entry(0xE2, 4),
                entry(0xE3, 4),
                entry(0xE4, 4),
                entry(0xE5, 4),
                entry(0xE6, 4),
                entry(0xE7, 4),
                entry(0xE8, 4),
                entry(0xE9, 4)
        );
    }

    public int length(int tag) {
        return tagLengthMap.get(tag);
    }

    public Integer tag01(ByteBuf byteBuf) {
        return (int) byteBuf.readUnsignedByte();
    }

    public Integer tag02(ByteBuf byteBuf) {
        return tag01(byteBuf);
    }

    public String tag03(ByteBuf byteBuf) {
        /*byte[] asciiBytes = new byte[15];
        for (int i = 0; i < 15; i++) {
            asciiBytes[i] = byteBuf.readByte();
        }
        return new String(asciiBytes, StandardCharsets.US_ASCII);*/
        return byteBuf.readCharSequence(tagLengthMap.get(0x03), StandardCharsets.US_ASCII).toString();
    }

    public Integer tag04(ByteBuf byteBuf) {
        return byteBuf.readUnsignedShortLE();
    }

    public Integer tag10(ByteBuf byteBuf) {
        return byteBuf.readUnsignedShortLE();
    }

    public Date tag20(ByteBuf byteBuf) {
        long secondsNum = byteBuf.readUnsignedIntLE();
        return new Date(secondsNum * 1000);
    }

    public Location tag30(ByteBuf byteBuf) {
        byte firstByte = byteBuf.readByte();
        Integer satellites = firstByte & 0xf;
        Integer correctness = (firstByte & 0xf0) >> 4;
        Double latitude = byteBuf.readIntLE() / 1_000_000.;
        Double longitude = byteBuf.readIntLE() / 1_000_000.;
        Boolean isCorrect = correctness == 0 || correctness == 2;
        return new Location(latitude, longitude, satellites, correctness, isCorrect);
    }
    
    public static void tagFE(ByteBuf byteBuf) {
        int extTagLength = byteBuf.readShortLE();
        //TODO сделать разбор расширенных тэгов. Здесь - временный вариант пропуска байт
        byteBuf.skipBytes(extTagLength);
    }

    /*
     @Getter
    @AllArgsConstructor
    public static class MotionInfo {
        private double speed;
        private double course;
    }

    public MotionInfo tag33(int dataIndex) {
        double speed = unsignedShort(bytes[dataIndex], bytes[dataIndex + 1]) / 10.;
        double course = unsignedShort(bytes[dataIndex + 2], bytes[dataIndex + 3]) / 10.;
        return new MotionInfo(speed, course);
    }

    public int tag34(int dataIndex) {
        return signedShort(bytes[dataIndex], bytes[dataIndex + 1]);
    }

    public int tag35(int dataIndex) {
        return unsignedByte(bytes[dataIndex]);
    }

    public int unsignedByte(byte b) {
        return b & 0xff;
    }

    public int unsignedShort(byte lowByte, byte highByte) {
        int low = unsignedByte(lowByte);
        int high = unsignedByte(highByte);
        return high << 8 | low;
    }

    public long unsignedInt(byte b1, byte b2, byte b3, byte b4) {
        long b1ToLong = b1 & 0xffL;
        long b2ToLong = b2 & 0xffL;
        long b3ToLong = b3 & 0xffL;
        long b4ToLong = b4 & 0xffL;
        return (b4ToLong << 24) | (b3ToLong << 16) | (b2ToLong << 8) | (b1ToLong);
    }

    public short signedShort(byte lowByte, byte highByte) {
        return (short) unsignedShort(lowByte, highByte);
    }

    public int signedInt(byte b1, byte b2, byte b3, byte b4) {
        int b1ToInt = unsignedByte(b1);
        int b2ToInt = unsignedByte(b2);
        int b3ToInt = unsignedByte(b3);
        int b4ToInt = unsignedByte(b4);
        return (b4ToInt << 24) | (b3ToInt << 16) | (b2ToInt << 8) | (b1ToInt);
    }

    public int maskHighBitInUnsignedShort(int intValue) {
        return intValue & 0x7FFF;
    }

    public int maskHigh4Bits(byte b) {
        return b & 0xf;
    }

    public int maskLow4Bits(byte b) {
        return (b & 0xf0) >> 4;
    }
     */
}
