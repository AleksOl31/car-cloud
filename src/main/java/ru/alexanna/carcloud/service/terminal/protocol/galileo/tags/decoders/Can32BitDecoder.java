package ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.decoders;

import io.netty.buffer.ByteBuf;
import ru.alexanna.carcloud.dto.MonitoringPackage;
import ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.Tag;
import ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.TagGroupDecoder;

public class Can32BitDecoder extends TagGroupDecoder {

    public Can32BitDecoder(int tagCode, MonitoringPackage sourceMonitoringPackage) {
        super(tagCode, sourceMonitoringPackage);
    }

    @Override
    public MonitoringPackage decode(ByteBuf byteBuf) {
        return getCan32Bit(byteBuf);
    }

    private MonitoringPackage getCan32Bit(ByteBuf byteBuf) {
        switch (Tag.fromCode(tagCode)) {
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
                sourceMonitoringPackage.getCan32BitList().add(tagCAN_32_BIT(byteBuf));
                break;
            default:
                byteBuf.skipBytes(Tag.fromCode(tagCode).getLength());
        }
        return sourceMonitoringPackage;
    }

    private static int tagCAN_32_BIT(ByteBuf byteBuf) {
        return byteBuf.readIntLE();
    }
}
