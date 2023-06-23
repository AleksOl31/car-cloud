package ru.alexanna.carcloud.service.terminal.protocol.galileo;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.alexanna.carcloud.dto.FuelSensor;
import ru.alexanna.carcloud.dto.TempSensor;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @AllArgsConstructor
    @Getter
    @Setter
    public static class Location {
        private Double latitude;
        private Double longitude;
        private Integer satellitesNum;
        private Integer correctness;
        private Boolean correct;

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

    // User tags
    public static int tagE2_E9(ByteBuf byteBuf) {
        return byteBuf.readInt();
    }

    public static int tagAnalogInputs(ByteBuf byteBuf) {
        return byteBuf.readUnsignedShortLE();
    }

    // RS485[0] - RS485[2]: ДУТ без температуры
    public static FuelSensor tag60_62(ByteBuf byteBuf) {
        int fuelLevel = byteBuf.readUnsignedShortLE();
        return new FuelSensor(fuelLevel);
    }

    // RS485[3] - RS485[15]: ДУТ с температурой
    public static FuelSensor tag63_6F(ByteBuf byteBuf) {
        int fuelLevel = byteBuf.readUnsignedShortLE();
        int fuelTemp = byteBuf.readByte();
        return new FuelSensor(fuelLevel, fuelTemp);
    }

    // 1-Wire: Термодатчики
    public static TempSensor tag70_77(ByteBuf byteBuf) {
        int id = byteBuf.readUnsignedByte();
        int temp = byteBuf.readByte();
        return new TempSensor(id, temp);
    }

    //CAN8BIT R15 - CAN8BIT R30
    public static int tagA0_AF(ByteBuf byteBuf) {
        return byteBuf.readByte();
    }

    //CAN16BIT R5 - CAN16BIT R14
    public static int tagB0_B9(ByteBuf byteBuf) {
        return byteBuf.readShortLE();
    }

    //CAN32BIT R5 - CAN32BIT R14
    public static int tagF0_F9(ByteBuf byteBuf) {
        return byteBuf.readIntLE();
    }
    
    public static List<Double> tagFE(ByteBuf byteBuf) {
        List<Double> extendedTags = new ArrayList<>();
        int extTagLength = byteBuf.readShortLE();
        int bytesCount = 0;
        //FIXME обработать все варианты тэгов (Здесь только 4 байтные)
        while (bytesCount < extTagLength) {
            int tagNum = byteBuf.readUnsignedShortLE();
            int tagValue = byteBuf.readIntLE();
            extendedTags.add(tagValue / 100.);
            bytesCount += 6;
        }
//        byteBuf.skipBytes(extTagLength);
        return extendedTags;
    }

}
