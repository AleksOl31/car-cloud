package ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.decoders;

import io.netty.buffer.ByteBuf;
import ru.alexanna.carcloud.dto.MonitoringPackage;
import ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.Tag;
import ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.TagGroupDecoder;

public class Can8BitDecoder extends TagGroupDecoder {

    public Can8BitDecoder(int tagCode, MonitoringPackage sourceMonitoringPackage) {
        super(tagCode, sourceMonitoringPackage);
    }

    @Override
    public MonitoringPackage decode(ByteBuf byteBuf) {
        return getCan8Bit(byteBuf);
    }

    private MonitoringPackage getCan8Bit(ByteBuf byteBuf) {
        switch (Tag.fromCode(tagCode)) {
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
                sourceMonitoringPackage.getCan8BitList().add(tagCAN_8_BIT(byteBuf));
                break;
            default:
                byteBuf.skipBytes(Tag.fromCode(tagCode).getLength());
        }
        return sourceMonitoringPackage;
    }

    private static int tagCAN_8_BIT(ByteBuf byteBuf) {
        return byteBuf.readByte();
    }
}
