package ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.decoders;

import io.netty.buffer.ByteBuf;
import ru.alexanna.carcloud.dto.MonitoringPackage;
import ru.alexanna.carcloud.dto.TempSensor;
import ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.Tag;
import ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.TagGroupDecoder;

public class WireOneDecoder extends TagGroupDecoder {

    public WireOneDecoder(int tagCode, MonitoringPackage sourceMonitoringPackage) {
        super(tagCode, sourceMonitoringPackage);
    }

    @Override
    public MonitoringPackage decode(ByteBuf byteBuf) {
        return get1Wire(byteBuf);
    }

    private MonitoringPackage get1Wire(ByteBuf byteBuf) {
        switch (Tag.fromCode(tagCode)) {
            case TEMP_0:
            case TEMP_1:
            case TEMP_2:
            case TEMP_3:
            case TEMP_4:
            case TEMP_5:
            case TEMP_6:
            case TEMP_7:
                sourceMonitoringPackage.getTempSensors().add(tagTEMP(byteBuf));
                break;
            default:
                byteBuf.skipBytes(Tag.fromCode(tagCode).getLength());
        }
        return sourceMonitoringPackage;
    }

    private static TempSensor tagTEMP(ByteBuf byteBuf) {
        int id = byteBuf.readUnsignedByte();
        int temp = byteBuf.readByte();
        return new TempSensor(id, temp);
    }
}
