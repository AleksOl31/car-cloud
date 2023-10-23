package ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.decoders;

import io.netty.buffer.ByteBuf;
import ru.alexanna.carcloud.dto.MonitoringPackage;
import ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.Tag;
import ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.TagGroupDecoder;

public class AdditionalDecoder extends TagGroupDecoder {

    public AdditionalDecoder(int tagCode, MonitoringPackage sourceMonitoringPackage) {
        super(tagCode, sourceMonitoringPackage);
    }

    @Override
    public MonitoringPackage decode(ByteBuf byteBuf) {
        return getAdditional(byteBuf);
    }

    private MonitoringPackage getAdditional(ByteBuf byteBuf) {
        switch (Tag.fromCode(tagCode)) {
            case ECO_DRIVE:
            case REP_500:
            case REFRIGERATOR_DATA:
            default:
                byteBuf.skipBytes(Tag.fromCode(tagCode).getLength());
        }
//        byteBuf.skipBytes(Tag.fromCode(tagCode).getLength());
        return sourceMonitoringPackage;
    }
}
