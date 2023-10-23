package ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.decoders;

import io.netty.buffer.ByteBuf;
import ru.alexanna.carcloud.dto.MonitoringPackage;
import ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.Tag;
import ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.TagGroupDecoder;

import java.nio.charset.StandardCharsets;

public class RegInfoDecoder extends TagGroupDecoder {

    public RegInfoDecoder(int tagCode, MonitoringPackage sourceMonitoringPackage) {
        super(tagCode, sourceMonitoringPackage);
    }

    @Override
    public MonitoringPackage decode(ByteBuf byteBuf) {
        return getRegInfo(byteBuf);
    }

    private MonitoringPackage getRegInfo(ByteBuf byteBuf) {
        switch (Tag.fromCode(tagCode)) {
            case HARD_VER:
                sourceMonitoringPackage.getRegInfo().setHardVer(tagHARD_VER(byteBuf));
                break;
            case SOFT_VER:
                sourceMonitoringPackage.getRegInfo().setSoftVer(tagSOFT_VER(byteBuf));
                break;
            case IMEI:
                sourceMonitoringPackage.getRegInfo().setImei(tagIMEI(byteBuf));
                break;
            case DEVICE_ID:
                sourceMonitoringPackage.getRegInfo().setDeviceId(tagDEVICE_ID(byteBuf));
                break;
            default:
                byteBuf.skipBytes(Tag.fromCode(tagCode).getLength());
        }
        return sourceMonitoringPackage;
    }

    private static Integer tagHARD_VER(ByteBuf byteBuf) {
        return (int) byteBuf.readUnsignedByte();
    }

    private static Integer tagSOFT_VER(ByteBuf byteBuf) {
        return tagHARD_VER(byteBuf);
    }

    private static String tagIMEI(ByteBuf byteBuf) {
        return byteBuf.readCharSequence(Tag.fromCode(0x03).getLength(), StandardCharsets.US_ASCII).toString();
    }

    private static Integer tagDEVICE_ID(ByteBuf byteBuf) {
        return byteBuf.readUnsignedShortLE();
    }
}
