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

    public static Integer tagHARD_VER(ByteBuf byteBuf) {
        return (int) byteBuf.readUnsignedByte();
    }

    public static Integer tagSOFT_VER(ByteBuf byteBuf) {
        return tagHARD_VER(byteBuf);
    }

    public static String tagIMEI(ByteBuf byteBuf) {
        return byteBuf.readCharSequence(Tag.fromCode(0x03).getLength(), StandardCharsets.US_ASCII).toString();
    }

    public static Integer tagDEVICE_ID(ByteBuf byteBuf) {
        return byteBuf.readUnsignedShortLE();
    }

    public static Integer tagRECORD_NUMBER(ByteBuf byteBuf) {
        return byteBuf.readUnsignedShortLE();
    }

    public static Date tagTIMESTAMP(ByteBuf byteBuf) {
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

    public static Location tagLOCATION(ByteBuf byteBuf) {
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

    public static MotionInfo tagMOTION_INFO(ByteBuf byteBuf) {
        double speed = byteBuf.readUnsignedShortLE() / 10.;
        double course = byteBuf.readUnsignedShortLE() / 10.;
        return new MotionInfo(speed, course);
    }

    public static int tagHEIGHT(ByteBuf byteBuf) {
        return byteBuf.readShortLE();
    }

    public static int tagHDOP(ByteBuf byteBuf) {
        return byteBuf.readUnsignedByte();
    }

    public static int tagDEV_STATUS(ByteBuf byteBuf) {
        return byteBuf.readUnsignedShortLE();
    }

    public static int tagSUPPLY_VOLTAGE(ByteBuf byteBuf) {
        return byteBuf.readUnsignedShortLE();
    }

    public static int tagBATTERY_VOLTAGE(ByteBuf byteBuf) {
        return byteBuf.readUnsignedShortLE();
    }

    public static int tagDEVICE_TEMP(ByteBuf byteBuf) {
        return byteBuf.readByte();
    }

    public static int tagUSER(ByteBuf byteBuf) {
        return byteBuf.readInt();
    }

    public static int tagAnalogInputs(ByteBuf byteBuf) {
        return byteBuf.readUnsignedShortLE();
    }

    public static FuelSensor tagRS485WithoutTemp(ByteBuf byteBuf, int tag) {
        int fuelLevel = byteBuf.readUnsignedShortLE();
        int address = tag - 0x60;
        return new FuelSensor(address, fuelLevel, -128);
    }

    public static FuelSensor tagRS485WithTemp(ByteBuf byteBuf, int tag) {
        int address = tag - 0x60;
        int fuelLevel = byteBuf.readUnsignedShortLE();
        int fuelTemp = byteBuf.readByte();
        return new FuelSensor(address, fuelLevel, fuelTemp);
    }

    public static TempSensor tagTEMP(ByteBuf byteBuf) {
        int id = byteBuf.readUnsignedByte();
        int temp = byteBuf.readByte();
        return new TempSensor(id, temp);
    }

    public static int tagCAN_8_BIT(ByteBuf byteBuf) {
        return byteBuf.readByte();
    }

    public static int tagCAN_16_BIT(ByteBuf byteBuf) {
        return byteBuf.readShortLE();
    }

    public static int tagCAN_32_BIT(ByteBuf byteBuf) {
        return byteBuf.readIntLE();
    }

    public static List<Double> tagEXTENDED(ByteBuf byteBuf) {
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


    public static void setMonitoringPackageData(MonitoringPackage monitoringPackage, int tagCode, ByteBuf byteBuf) {
        Tag tag = Tag.fromCode(tagCode);
//        System.out.println(TagGroup.fromCode(tagCode) + " " + tag);
        switch (tag) {
            case HARD_VER:
                monitoringPackage.getRegInfo().setHardVer(GalileoTagDecoder.tagHARD_VER(byteBuf));
                break;
            case SOFT_VER:
                monitoringPackage.getRegInfo().setSoftVer(GalileoTagDecoder.tagSOFT_VER(byteBuf));
                break;
            case IMEI:
                monitoringPackage.getRegInfo().setImei(GalileoTagDecoder.tagIMEI(byteBuf));
                break;
            case DEVICE_ID:
                monitoringPackage.getRegInfo().setDeviceId(GalileoTagDecoder.tagDEVICE_ID(byteBuf));
                break;
            case RECORD_NUMBER:
                monitoringPackage.getDeviceInfo().setRecordNum(GalileoTagDecoder.tagRECORD_NUMBER(byteBuf));
                break;
            case TIMESTAMP:
                monitoringPackage.setCreatedAt(GalileoTagDecoder.tagTIMESTAMP(byteBuf));
                break;
            case LOCATION:
                GalileoTagDecoder.Location location = GalileoTagDecoder.tagLOCATION(byteBuf);
                monitoringPackage.getNavigationInfo().setLatitude(location.getLatitude());
                monitoringPackage.getNavigationInfo().setLongitude(location.getLongitude());
                monitoringPackage.getNavigationInfo().setSatellitesNum(location.getSatellitesNum());
                monitoringPackage.getNavigationInfo().setCorrectness(location.getCorrectness());
                monitoringPackage.getNavigationInfo().setCorrect(location.getCorrect());
                break;
            case MOTION_INFO:
                GalileoTagDecoder.MotionInfo motionInfo = GalileoTagDecoder.tagMOTION_INFO(byteBuf);
                monitoringPackage.getNavigationInfo().setSpeed(motionInfo.getSpeed());
                monitoringPackage.getNavigationInfo().setCourse(motionInfo.getCourse());
                break;
            case HEIGHT:
                monitoringPackage.getNavigationInfo().setHeight(GalileoTagDecoder.tagHEIGHT(byteBuf));
                break;
            case HDOP:
                int dop = GalileoTagDecoder.tagHDOP(byteBuf);
                if (monitoringPackage.getNavigationInfo().getCorrect()) {
                    if (monitoringPackage.getNavigationInfo().getCorrectness() == 0)
                        monitoringPackage.getNavigationInfo().setHdop(dop / 10.);
                    else if (monitoringPackage.getNavigationInfo().getCorrectness() == 2)
                        monitoringPackage.getNavigationInfo().setHdop(dop * 10.);
                }
                break;
            case DEVICE_STATUS:
                monitoringPackage.getDeviceInfo().setStatus(GalileoTagDecoder.tagDEV_STATUS(byteBuf));
                break;
            case SUPPLY_VOLTAGE:
                monitoringPackage.getDeviceInfo().setSupplyVol(GalileoTagDecoder.tagSUPPLY_VOLTAGE(byteBuf));
                break;
            case BATTERY_VOLTAGE:
                monitoringPackage.getDeviceInfo().setBatteryVol(GalileoTagDecoder.tagBATTERY_VOLTAGE(byteBuf));
                break;
            case DEVICE_TEMP:
                monitoringPackage.getDeviceInfo().setDeviceTemp(GalileoTagDecoder.tagDEVICE_TEMP(byteBuf));
                break;
            case ANALOG_INPUT_0:
            case ANALOG_INPUT_1:
            case ANALOG_INPUT_2:
            case ANALOG_INPUT_3:
            case ANALOG_INPUT_4:
            case ANALOG_INPUT_5:
            case ANALOG_INPUT_6:
            case ANALOG_INPUT_7:
            case ANALOG_INPUT_8:
            case ANALOG_INPUT_9:
            case ANALOG_INPUT_10:
            case ANALOG_INPUT_11:
            case ANALOG_INPUT_12:
            case ANALOG_INPUT_13:
                monitoringPackage.getAnalogInputs().add(GalileoTagDecoder.tagAnalogInputs(byteBuf));
                break;
            case RS485_0:
            case RS485_1:
            case RS485_2:
                monitoringPackage.getFuelSensors().add(GalileoTagDecoder.tagRS485WithoutTemp(byteBuf, tagCode));
                break;
            case RS485_3:
            case RS485_4:
            case RS485_5:
            case RS485_6:
            case RS485_7:
            case RS485_8:
            case RS485_9:
            case RS485_10:
            case RS485_11:
            case RS485_12:
            case RS485_13:
            case RS485_14:
            case RS485_15:
                monitoringPackage.getFuelSensors().add(GalileoTagDecoder.tagRS485WithTemp(byteBuf, tagCode));
                break;
            case TEMP_0:
            case TEMP_1:
            case TEMP_2:
            case TEMP_3:
            case TEMP_4:
            case TEMP_5:
            case TEMP_6:
            case TEMP_7:
                monitoringPackage.getTempSensors().add(GalileoTagDecoder.tagTEMP(byteBuf));
                break;
            case CAN_8_BIT_R0:
            case CAN_8_BIT_R1:
            case CAN_8_BIT_R2:
            case CAN_8_BIT_R3:
            case CAN_8_BIT_R4:
            case CAN_8_BIT_R5:
            case CAN_8_BIT_R6:
            case CAN_8_BIT_R7:
            case CAN_8_BIT_R8:
            case CAN_8_BIT_R9:
            case CAN_8_BIT_R10:
            case CAN_8_BIT_R11:
            case CAN_8_BIT_R12:
            case CAN_8_BIT_R13:
            case CAN_8_BIT_R14:
            case CAN_8_BIT_R15:
            case CAN_8_BIT_R16:
            case CAN_8_BIT_R17:
            case CAN_8_BIT_R18:
            case CAN_8_BIT_R19:
            case CAN_8_BIT_R20:
            case CAN_8_BIT_R21:
            case CAN_8_BIT_R22:
            case CAN_8_BIT_R23:
            case CAN_8_BIT_R24:
            case CAN_8_BIT_R25:
            case CAN_8_BIT_R26:
            case CAN_8_BIT_R27:
            case CAN_8_BIT_R28:
            case CAN_8_BIT_R29:
            case CAN_8_BIT_R30:
                monitoringPackage.getCan8BitList().add(GalileoTagDecoder.tagCAN_8_BIT(byteBuf));
                break;
            case CAN_16_BIT_R0:
            case CAN_16_BIT_R1:
            case CAN_16_BIT_R2:
            case CAN_16_BIT_R3:
            case CAN_16_BIT_R4:
            case CAN_16_BIT_R5:
            case CAN_16_BIT_R6:
            case CAN_16_BIT_R7:
            case CAN_16_BIT_R8:
            case CAN_16_BIT_R9:
            case CAN_16_BIT_R10:
            case CAN_16_BIT_R11:
            case CAN_16_BIT_R12:
            case CAN_16_BIT_R13:
            case CAN_16_BIT_R14:
                monitoringPackage.getCan16BitList().add(GalileoTagDecoder.tagCAN_16_BIT(byteBuf));
                break;
            case CAN_32_BIT_R0:
            case CAN_32_BIT_R1:
            case CAN_32_BIT_R2:
            case CAN_32_BIT_R3:
            case CAN_32_BIT_R4:
            case CAN_32_BIT_R5:
            case CAN_32_BIT_R6:
            case CAN_32_BIT_R7:
            case CAN_32_BIT_R8:
            case CAN_32_BIT_R9:
            case CAN_32_BIT_R10:
            case CAN_32_BIT_R11:
            case CAN_32_BIT_R12:
            case CAN_32_BIT_R13:
            case CAN_32_BIT_R14:
                monitoringPackage.getCan32BitList().add(GalileoTagDecoder.tagCAN_32_BIT(byteBuf));
                break;
            case USER_0:
            case USER_1:
            case USER_2:
            case USER_3:
            case USER_4:
            case USER_5:
            case USER_6:
            case USER_7:
                monitoringPackage.getUserTags().add(GalileoTagDecoder.tagUSER(byteBuf));
                break;
            case EXTENDED:
                monitoringPackage.getExtendedTags().addAll(GalileoTagDecoder.tagEXTENDED(byteBuf));
                break;
            default:
                byteBuf.skipBytes(tag.getLength());
        }
    }


}
