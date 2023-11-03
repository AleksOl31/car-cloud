package ru.alexanna.carcloud.service.terminal.protocol.galileo.tags;

import io.netty.buffer.ByteBuf;
import ru.alexanna.carcloud.dto.MonitoringPackage;
import ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.decoders.*;

abstract public class TagGroupDecoder {

    protected final int tagCode;
    protected final MonitoringPackage sourceMonitoringPackage;

    public abstract MonitoringPackage decode(ByteBuf byteBuf);

    public TagGroupDecoder(int tagCode, MonitoringPackage sourceMonitoringPackage) {
        this.tagCode = tagCode;
        this.sourceMonitoringPackage = sourceMonitoringPackage;
    }
    public static TagGroupDecoder create(int tagCode, MonitoringPackage sourceMonitoringPackage) {
        switch (TagGroup.fromCode(tagCode)) {
            case REG_INFO:
                return new RegInfoDecoder(tagCode, sourceMonitoringPackage);
            case DEVICE_INFO:
                return new DeviceInfoDecoder(tagCode, sourceMonitoringPackage);
            case NAVIGATION_INFO:
                return new NavigationInfoDecoder(tagCode, sourceMonitoringPackage);
            case ANALOG_INPUTS:
                return new AnalogInputsDecoder(tagCode, sourceMonitoringPackage);
            case DIGITAL_PORTS:
                return new DigitalPortsDecoder(tagCode, sourceMonitoringPackage);
            case WIRE_1:
                return new WireOneDecoder(tagCode, sourceMonitoringPackage);
            case DS_1923:
                return new Ds1923Decoder(tagCode, sourceMonitoringPackage);
            case CAN_STANDARD:
                return new CanStandardDecoder(tagCode, sourceMonitoringPackage);
            case CAN_8_BIT:
                return new Can8BitDecoder(tagCode, sourceMonitoringPackage);
            case CAN_16_BIT:
                return new Can16BitDecoder(tagCode, sourceMonitoringPackage);
            case CAN_32_BIT:
                return new Can32BitDecoder(tagCode, sourceMonitoringPackage);
            case ADDITIONAL:
                return new AdditionalDecoder(tagCode, sourceMonitoringPackage);
            case USER_TAGS:
                return new UserTagsDecoder(tagCode, sourceMonitoringPackage);
            case EXTENDED_TAGS:
                return new ExtendedTagsDecoder(tagCode, sourceMonitoringPackage);
            default:
                throw new IllegalArgumentException("Invalid tag code: " + Integer.toHexString(tagCode));
        }

    }
}
