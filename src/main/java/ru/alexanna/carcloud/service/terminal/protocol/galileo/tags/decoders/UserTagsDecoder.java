package ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.decoders;

import io.netty.buffer.ByteBuf;
import ru.alexanna.carcloud.dto.MonitoringPackage;
import ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.Tag;
import ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.TagGroupDecoder;

public class UserTagsDecoder extends TagGroupDecoder {

    public UserTagsDecoder(int tagCode, MonitoringPackage sourceMonitoringPackage) {
        super(tagCode, sourceMonitoringPackage);
    }

    @Override
    public MonitoringPackage decode(ByteBuf byteBuf) {
        return getUserTags(byteBuf);
    }

    private MonitoringPackage getUserTags(ByteBuf byteBuf) {
        switch (Tag.fromCode(tagCode)) {
            case USER_0:
            case USER_1:
            case USER_2:
            case USER_3:
            case USER_4:
            case USER_5:
            case USER_6:
            case USER_7:
                sourceMonitoringPackage.getUserTags().add(tagUSER(byteBuf));
                break;
            default:
                byteBuf.skipBytes(Tag.fromCode(tagCode).getLength());
        }
        return sourceMonitoringPackage;
    }

    private static int tagUSER(ByteBuf byteBuf) {
        return byteBuf.readInt();
    }
}
