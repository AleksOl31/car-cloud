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

    public static int tag34(ByteBuf byteBuf) {
        return byteBuf.readShortLE();
    }

    public static int tag35(ByteBuf byteBuf) {
        return byteBuf.readUnsignedByte();
    }

    public static int tag40(ByteBuf byteBuf) {
        return byteBuf.readUnsignedShortLE();
    }

    public static int tag41(ByteBuf byteBuf) {
        return byteBuf.readUnsignedShortLE();
    }

    public static int tag42(ByteBuf byteBuf) {
        return byteBuf.readUnsignedShortLE();
    }

    public static int tag43(ByteBuf byteBuf) {
        return byteBuf.readByte();
    }

    public static int tagE2_E9(ByteBuf byteBuf) {
        return byteBuf.readInt();
    }
    
    public static void tagFE(ByteBuf byteBuf) {
        int extTagLength = byteBuf.readShortLE();
        //TODO сделать разбор расширенных тэгов. Здесь - временный вариант пропуска байт
        byteBuf.skipBytes(extTagLength);
    }

}
