package ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.decoders;

import io.netty.buffer.ByteBuf;
import ru.alexanna.carcloud.dto.MonitoringPackage;
import ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.Tag;
import ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.TagGroupDecoder;

public class AnalogInputsDecoder extends TagGroupDecoder {

    public AnalogInputsDecoder(int tagCode, MonitoringPackage sourceMonitoringPackage) {
        super(tagCode, sourceMonitoringPackage);
    }

    @Override
    public MonitoringPackage decode(ByteBuf byteBuf) {
        return getAnalogInputs(byteBuf);
    }

    private MonitoringPackage getAnalogInputs(ByteBuf byteBuf) {
        switch (Tag.fromCode(tagCode)) {
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
                sourceMonitoringPackage.getAnalogInputs().add(tagAnalogInputs(byteBuf));
                break;
            default:
                byteBuf.skipBytes(Tag.fromCode(tagCode).getLength());
        }
        return sourceMonitoringPackage;
    }

    private static int tagAnalogInputs(ByteBuf byteBuf) {
        return byteBuf.readUnsignedShortLE();
    }
}
