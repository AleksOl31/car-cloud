package ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.decoders;

import io.netty.buffer.ByteBuf;
import ru.alexanna.carcloud.dto.MonitoringPackage;
import ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.Tag;
import ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.TagGroupDecoder;

public class CanStandardDecoder extends TagGroupDecoder {

    public CanStandardDecoder(int tagCode, MonitoringPackage sourceMonitoringPackage) {
        super(tagCode, sourceMonitoringPackage);
    }

    @Override
    public MonitoringPackage decode(ByteBuf byteBuf) {
        return getCanStandard(byteBuf);
    }

    private MonitoringPackage getCanStandard(ByteBuf byteBuf) {
        switch (Tag.fromCode(tagCode)) {
            case CAN_A0:
            case CAN_A1:
            case CAN_B0:
            case CAN_B1:
            default:
                byteBuf.skipBytes(Tag.fromCode(tagCode).getLength());
        }
//        byteBuf.skipBytes(Tag.fromCode(tagCode).getLength());
        return sourceMonitoringPackage;
    }
}
