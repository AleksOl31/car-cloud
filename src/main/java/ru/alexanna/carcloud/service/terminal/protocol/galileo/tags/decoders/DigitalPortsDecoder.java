package ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.decoders;

import io.netty.buffer.ByteBuf;
import ru.alexanna.carcloud.dto.FuelSensor;
import ru.alexanna.carcloud.dto.MonitoringPackage;
import ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.Tag;
import ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.TagGroupDecoder;

public class DigitalPortsDecoder extends TagGroupDecoder {

    public DigitalPortsDecoder(int tagCode, MonitoringPackage sourceMonitoringPackage) {
        super(tagCode, sourceMonitoringPackage);
    }

    @Override
    public MonitoringPackage decode(ByteBuf byteBuf) {
        return getDigitalPorts(byteBuf);
    }

    private MonitoringPackage getDigitalPorts(ByteBuf byteBuf) {
        switch (Tag.fromCode(tagCode)) {
            case RS485_0:
            case RS485_1:
            case RS485_2:
                sourceMonitoringPackage.getFuelSensors().add(tagRS485WithoutTemp(byteBuf, tagCode));
                break;
            case RS485_3:
            case RS485_4:
            case RS485_5:
            case RS485_6:
            case RS485_7:
            case RS485_8:
            case RS485_9:
            case RS485_10:
            case RS485_11:
            case RS485_12:
            case RS485_13:
            case RS485_14:
            case RS485_15:
                sourceMonitoringPackage.getFuelSensors().add(tagRS485WithTemp(byteBuf, tagCode));
                break;
            default:
                byteBuf.skipBytes(Tag.fromCode(tagCode).getLength());
        }
        return sourceMonitoringPackage;
    }

    private static FuelSensor tagRS485WithoutTemp(ByteBuf byteBuf, int tag) {
        int fuelLevel = byteBuf.readUnsignedShortLE();
        int address = tag - 0x60;
        return new FuelSensor(address, fuelLevel, -128);
    }

    private static FuelSensor tagRS485WithTemp(ByteBuf byteBuf, int tag) {
        int address = tag - 0x60;
        int fuelLevel = byteBuf.readUnsignedShortLE();
        int fuelTemp = byteBuf.readByte();
        return new FuelSensor(address, fuelLevel, fuelTemp);
    }
}
