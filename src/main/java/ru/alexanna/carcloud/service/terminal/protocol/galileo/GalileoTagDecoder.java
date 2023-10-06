package ru.alexanna.carcloud.service.terminal.protocol.galileo;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.alexanna.carcloud.dto.FuelSensor;
import ru.alexanna.carcloud.dto.MonitoringPackage;
import ru.alexanna.carcloud.dto.TempSensor;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GalileoTagDecoder {

    // Hard version
    public static Integer tag01(ByteBuf byteBuf) {
        return (int) byteBuf.readUnsignedByte();
    }

    // Soft version
    public static Integer tag02(ByteBuf byteBuf) {
        return tag01(byteBuf);
    }

    // IMEI
    public static String tag03(ByteBuf byteBuf) {
        return byteBuf.readCharSequence(GalileoTag.length(0x03), StandardCharsets.US_ASCII).toString();
    }

    // Device ID
    public static Integer tag04(ByteBuf byteBuf) {
        return byteBuf.readUnsignedShortLE();
    }

    // Record number
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

    // Height, m
    public static int tag34(ByteBuf byteBuf) {
        return byteBuf.readShortLE();
    }

    // HDOP
    public static int tag35(ByteBuf byteBuf) {
        return byteBuf.readUnsignedByte();
    }

    // Device status
    public static int tag40(ByteBuf byteBuf) {
        return byteBuf.readUnsignedShortLE();
    }

    // Supply voltage
    public static int tag41(ByteBuf byteBuf) {
        return byteBuf.readUnsignedShortLE();
    }

    // Battery voltage
    public static int tag42(ByteBuf byteBuf) {
        return byteBuf.readUnsignedShortLE();
    }

    // Device temperature
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
    public static FuelSensor tag60_62(ByteBuf byteBuf, int tag) {
        int fuelLevel = byteBuf.readUnsignedShortLE();
        int address = tag - 0x60;
        return new FuelSensor(address, fuelLevel, -128);
    }

    // RS485[3] - RS485[15]: ДУТ с температурой
    public static FuelSensor tag63_6F(ByteBuf byteBuf, int tag) {
        int address = tag - 0x60;
        int fuelLevel = byteBuf.readUnsignedShortLE();
        int fuelTemp = byteBuf.readByte();
        return new FuelSensor(address, fuelLevel, fuelTemp);
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

    //EXTENDED TAGS
    public static List<Double> tagFE(ByteBuf byteBuf) {
        List<Double> extendedTags = new ArrayList<>();
        int extTagLength = byteBuf.readShortLE();
        int bytesCount = 0;
        // FIXME обработать все варианты тэгов (Здесь только 4 байтные)
        while (bytesCount < extTagLength) {
            int tagNum = byteBuf.readUnsignedShortLE();
            int tagValue = byteBuf.readIntLE();
            if (tagValue == Integer.MAX_VALUE) {
                extendedTags.add(null);
            } else {
                extendedTags.add(tagValue / 100.);
            }
            bytesCount += 6;
        }
//        byteBuf.skipBytes(extTagLength);
        return extendedTags;
    }


    public static void setMonitoringPackageData(MonitoringPackage monitoringPackage, int tag, ByteBuf byteBuf) {
        switch (tag) {
            case 0x01:
                monitoringPackage.getRegInfo().setHardVer(GalileoTagDecoder.tag01(byteBuf));
                break;
            case 0x02:
                monitoringPackage.getRegInfo().setSoftVer(GalileoTagDecoder.tag02(byteBuf));
                break;
            case 0x03:
                monitoringPackage.getRegInfo().setImei(GalileoTagDecoder.tag03(byteBuf));
                break;
            case 0x04:
                monitoringPackage.getRegInfo().setDeviceId(GalileoTagDecoder.tag04(byteBuf));
                break;
            case 0x10:
                monitoringPackage.getDeviceInfo().setRecordNum(GalileoTagDecoder.tag10(byteBuf));
                break;
            case 0x20:
                monitoringPackage.setCreatedAt(GalileoTagDecoder.tag20(byteBuf));
                break;
            case 0x30:
                GalileoTagDecoder.Location location = GalileoTagDecoder.tag30(byteBuf);
                monitoringPackage.getNavigationInfo().setLatitude(location.getLatitude());
                monitoringPackage.getNavigationInfo().setLongitude(location.getLongitude());
                monitoringPackage.getNavigationInfo().setSatellitesNum(location.getSatellitesNum());
                monitoringPackage.getNavigationInfo().setCorrectness(location.getCorrectness());
                monitoringPackage.getNavigationInfo().setCorrect(location.getCorrect());
                break;
            case 0x33:
                GalileoTagDecoder.MotionInfo motionInfo = GalileoTagDecoder.tag33(byteBuf);
                monitoringPackage.getNavigationInfo().setSpeed(motionInfo.getSpeed());
                monitoringPackage.getNavigationInfo().setCourse(motionInfo.getCourse());
                break;
            case 0x34:
                monitoringPackage.getNavigationInfo().setHeight(GalileoTagDecoder.tag34(byteBuf));
                break;
            case 0x35:
                int dop = GalileoTagDecoder.tag35(byteBuf);
                if (monitoringPackage.getNavigationInfo().getCorrect()) {
                    if (monitoringPackage.getNavigationInfo().getCorrectness() == 0)
                        monitoringPackage.getNavigationInfo().setHdop(dop / 10.);
                    else if (monitoringPackage.getNavigationInfo().getCorrectness() == 2)
                        monitoringPackage.getNavigationInfo().setHdop(dop * 10.);
                }
                break;
            case 0x40:
                monitoringPackage.getDeviceInfo().setStatus(GalileoTagDecoder.tag40(byteBuf));
                break;
            case 0x41:
                monitoringPackage.getDeviceInfo().setSupplyVol(GalileoTagDecoder.tag41(byteBuf));
                break;
            case 0x42:
                monitoringPackage.getDeviceInfo().setBatteryVol(GalileoTagDecoder.tag42(byteBuf));
                break;
            case 0x43:
                monitoringPackage.getDeviceInfo().setDeviceTemp(GalileoTagDecoder.tag43(byteBuf));
                break;
            case 0x50:
            case 0x51:
            case 0x52:
            case 0x53:
            case 0x54:
            case 0x55:
            case 0x56:
            case 0x57:
            case 0x78:
            case 0x79:
            case 0x7A:
            case 0x7B:
            case 0x7C:
            case 0x7D:
                monitoringPackage.getAnalogInputs().add(GalileoTagDecoder.tagAnalogInputs(byteBuf));
                break;
            case 0x60:
            case 0x61:
            case 0x62:
                monitoringPackage.getFuelSensors().add(GalileoTagDecoder.tag60_62(byteBuf, tag));
                break;
            case 0x63:
            case 0x64:
            case 0x65:
            case 0x66:
            case 0x67:
            case 0x68:
            case 0x69:
            case 0x6A:
            case 0x6B:
            case 0x6C:
            case 0x6D:
            case 0x6E:
            case 0x6F:
                monitoringPackage.getFuelSensors().add(GalileoTagDecoder.tag63_6F(byteBuf, tag));
                break;
            case 0x70:
            case 0x71:
            case 0x72:
            case 0x73:
            case 0x74:
            case 0x75:
            case 0x76:
            case 0x77:
                monitoringPackage.getTempSensors().add(GalileoTagDecoder.tag70_77(byteBuf));
                break;
            case 0xA0:
            case 0xA1:
            case 0xA2:
            case 0xA3:
            case 0xA4:
            case 0xA5:
            case 0xA6:
            case 0xA7:
            case 0xA8:
            case 0xA9:
            case 0xAA:
            case 0xAB:
            case 0xAC:
            case 0xAD:
            case 0xAE:
            case 0xAF:
                monitoringPackage.getCan8BitList().add(GalileoTagDecoder.tagA0_AF(byteBuf));
                break;
            case 0xB0:
            case 0xB1:
            case 0xB2:
            case 0xB3:
            case 0xB4:
            case 0xB5:
            case 0xB6:
            case 0xB7:
            case 0xB8:
            case 0xB9:
                monitoringPackage.getCan16BitList().add(GalileoTagDecoder.tagB0_B9(byteBuf));
                break;
            case 0xF0:
            case 0xF1:
            case 0xF2:
            case 0xF3:
            case 0xF4:
            case 0xF5:
            case 0xF6:
            case 0xF7:
            case 0xF8:
            case 0xF9:
                monitoringPackage.getCan32BitList().add(GalileoTagDecoder.tagF0_F9(byteBuf));
                break;
            case 0xe2:
            case 0xe3:
            case 0xe4:
            case 0xe5:
            case 0xe6:
            case 0xe7:
            case 0xe8:
            case 0xe9:
                monitoringPackage.getUserTags().add(GalileoTagDecoder.tagE2_E9(byteBuf));
                break;
            case 0xfe:
                monitoringPackage.getExtendedTags().addAll(GalileoTagDecoder.tagFE(byteBuf));
                break;
            default:
                byteBuf.skipBytes(GalileoTag.length(tag));
        }
    }


}
