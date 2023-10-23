package ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.decoders;

import io.netty.buffer.ByteBuf;
import ru.alexanna.carcloud.dto.MonitoringPackage;
import ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.Tag;
import ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.TagGroupDecoder;

public class Ds1923Decoder extends TagGroupDecoder {

    public Ds1923Decoder(int tagCode, MonitoringPackage sourceMonitoringPackage) {
        super(tagCode, sourceMonitoringPackage);
    }

    @Override
    public MonitoringPackage decode(ByteBuf byteBuf) {
        return getDS1923(byteBuf);
    }

    private MonitoringPackage getDS1923(ByteBuf byteBuf) {
        switch (Tag.fromCode(tagCode)) {
            case DS1923_0:
            case DS1923_1:
            case DS1923_2:
            case DS1923_3:
            case DS1923_4:
            case DS1923_5:
            case DS1923_6:
            case DS1923_7:
            default:
                byteBuf.skipBytes(Tag.fromCode(tagCode).getLength());
        }
//        byteBuf.skipBytes(Tag.fromCode(tagCode).getLength());
        return sourceMonitoringPackage;
    }
}
