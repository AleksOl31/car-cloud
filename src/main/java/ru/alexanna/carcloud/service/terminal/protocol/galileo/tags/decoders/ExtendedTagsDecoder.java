package ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.decoders;

import io.netty.buffer.ByteBuf;
import ru.alexanna.carcloud.dto.MonitoringPackage;
import ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.Tag;
import ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.TagGroupDecoder;

import java.util.ArrayList;
import java.util.List;

public class ExtendedTagsDecoder extends TagGroupDecoder {

    public ExtendedTagsDecoder(int tagCode, MonitoringPackage sourceMonitoringPackage) {
        super(tagCode, sourceMonitoringPackage);
    }

    @Override
    public MonitoringPackage decode(ByteBuf byteBuf) {
        return getExtendedTags(byteBuf);
    }

    private MonitoringPackage getExtendedTags(ByteBuf byteBuf) {
        if (Tag.fromCode(tagCode) == Tag.EXTENDED) {
            sourceMonitoringPackage.getExtendedTags().addAll(tagEXTENDED(byteBuf));
        } else {
            byteBuf.skipBytes(Tag.fromCode(tagCode).getLength());
        }
        return sourceMonitoringPackage;
    }

    private static List<Double> tagEXTENDED(ByteBuf byteBuf) {
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
}
