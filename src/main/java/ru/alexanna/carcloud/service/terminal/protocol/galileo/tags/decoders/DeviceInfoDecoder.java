package ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.decoders;

import io.netty.buffer.ByteBuf;
import ru.alexanna.carcloud.dto.MonitoringPackage;
import ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.Tag;
import ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.TagGroupDecoder;

import java.util.Date;

public class DeviceInfoDecoder extends TagGroupDecoder {

    public DeviceInfoDecoder(int tagCode, MonitoringPackage sourceMonitoringPackage) {
        super(tagCode, sourceMonitoringPackage);
    }

    @Override
    public MonitoringPackage decode(ByteBuf byteBuf) {
        return getDeviceInfo(byteBuf);
    }

    private MonitoringPackage getDeviceInfo(ByteBuf byteBuf) {
        switch (Tag.fromCode(tagCode)) {
            case RECORD_NUMBER:
                sourceMonitoringPackage.getDeviceInfo().setRecordNum(tagRECORD_NUMBER(byteBuf));
                break;
            case TIMESTAMP:
                sourceMonitoringPackage.setCreatedAt(tagTIMESTAMP(byteBuf));
                break;
            case DEVICE_STATUS:
                sourceMonitoringPackage.getDeviceInfo().setStatus(tagDEV_STATUS(byteBuf));
                break;
            case SUPPLY_VOLTAGE:
                sourceMonitoringPackage.getDeviceInfo().setSupplyVol(tagSUPPLY_VOLTAGE(byteBuf));
                break;
            case BATTERY_VOLTAGE:
                sourceMonitoringPackage.getDeviceInfo().setBatteryVol(tagBATTERY_VOLTAGE(byteBuf));
                break;
            case DEVICE_TEMP:
                sourceMonitoringPackage.getDeviceInfo().setDeviceTemp(tagDEVICE_TEMP(byteBuf));
                break;
            default:
                byteBuf.skipBytes(Tag.fromCode(tagCode).getLength());
        }
        return sourceMonitoringPackage;
    }

    private static Integer tagRECORD_NUMBER(ByteBuf byteBuf) {
        return byteBuf.readUnsignedShortLE();
    }

    private static Date tagTIMESTAMP(ByteBuf byteBuf) {
        long secondsNum = byteBuf.readUnsignedIntLE();
        return new Date(secondsNum * 1000);
    }

    private static int tagSUPPLY_VOLTAGE(ByteBuf byteBuf) {
        return byteBuf.readUnsignedShortLE();
    }

    private static int tagBATTERY_VOLTAGE(ByteBuf byteBuf) {
        return byteBuf.readUnsignedShortLE();
    }

    private static int tagDEVICE_TEMP(ByteBuf byteBuf) {
        return byteBuf.readByte();
    }

    private static int tagDEV_STATUS(ByteBuf byteBuf) {
        return byteBuf.readUnsignedShortLE();
    }
}
