package ru.alexanna.carcloud.service.terminal.protocol;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.alexanna.carcloud.model.Location;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public class GalileoTagDecoder {

    public static Integer tag01(ByteBuf byteBuf) {
        return (int) byteBuf.readUnsignedByte();
    }

    public static Integer tag02(ByteBuf byteBuf) {
        return tag01(byteBuf);
    }

    public static String tag03(ByteBuf byteBuf) {
        return byteBuf.readCharSequence(GalileoTag.length(0x03), StandardCharsets.US_ASCII).toString();
    }

    public static Integer tag04(ByteBuf byteBuf) {
        return byteBuf.readUnsignedShortLE();
    }

    public static Integer tag10(ByteBuf byteBuf) {
        return byteBuf.readUnsignedShortLE();
    }

    public static Date tag20(ByteBuf byteBuf) {
        long secondsNum = byteBuf.readUnsignedIntLE();
        return new Date(secondsNum * 1000);
    }

    public static Location tag30(ByteBuf byteBuf) {
        byte firstByte = byteBuf.readByte();
        Integer satellites = firstByte & 0xf;
        int correctness = (firstByte & 0xf0) >> 4;
        Double latitude = byteBuf.readIntLE() / 1_000_000.;
        Double longitude = byteBuf.readIntLE() / 1_000_000.;
        Boolean isCorrect = correctness == 0 || correctness == 2;
        return new Location(latitude, longitude, satellites, correctness, isCorrect);
    }

    @Getter
    @AllArgsConstructor
    public static class MotionInfo {
        private double speed;
        private double course;
    }

    public static MotionInfo tag33(ByteBuf byteBuf) {
        double speed = byteBuf.readUnsignedShortLE() / 10.;
        double course = byteBuf.readUnsignedShortLE() / 10.;
        return new MotionInfo(speed, course);
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
