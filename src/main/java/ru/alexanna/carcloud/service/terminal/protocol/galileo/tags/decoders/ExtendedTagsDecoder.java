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
        if (Tag.fromCode(tagCode) == Tag.EXTENDED) {
            sourceMonitoringPackage.getExtendedTags().addAll(getExtendedParamsFrom(byteBuf));
        } else {
            byteBuf.skipBytes(Tag.fromCode(tagCode).getLength());
        }
        return sourceMonitoringPackage;
    }

    private List<Double> getExtendedParamsFrom(ByteBuf byteBuf) {
        List<Double> extendedTags = new ArrayList<>();
        int extTagLength = byteBuf.readShortLE();
        int bytesCount = 0;
        final int TAG_NUM_LENGTH = 2;
        while (bytesCount < extTagLength) {
            int tagNum = byteBuf.readUnsignedShortLE();
            if (isModbusOrBluetoothTags(tagNum)) {
                extendedTags.add(getModbusOrBluetoothParam(byteBuf));
            }
            else {
                byteBuf.skipBytes(getTagLength(tagNum));
            }
            bytesCount += (getTagLength(tagNum) + TAG_NUM_LENGTH);
        }
        return extendedTags;
    }

    private Double getModbusOrBluetoothParam(ByteBuf byteBuf) {
        int tagValue = byteBuf.readIntLE();
        return tagValue == Integer.MAX_VALUE ? null : tagValue / 100.;
    }

    private int getTagLength(int tagNum) {
        final int FOUR_BYTE_TAG = 4;
        if (isModbusOrBluetoothTags(tagNum) || isTempTags(tagNum))
            return FOUR_BYTE_TAG;
        else
            return ExtTag.fromCode(tagNum).getLength();
    }

    private boolean isModbusOrBluetoothTags(int tagNum) {
        return 0x01 <= tagNum && tagNum <= 0x80;
    }

    private boolean isTempTags(int tagNum) {
        return 0x86 <= tagNum && tagNum <= 0x8d;
    }

    public enum ExtTag {
        CID(0x81, 2),
        LAC(0x82, 2),
        MCC(0x83, 2),
        MNC(0x84, 2),
        RSSI(0x85, 1),
        GPS(0x8e, 4),
        GLONASS(0x8f, 4),
        BAIDOU(0x90, 4),
        GALILEO(0x91, 4),
        IMSI(0x92, 15);
        private final int code, length;

        ExtTag(int code, int length) {
            this.code = code;
            this.length = length;
        }

        public int getCode() {
            return code;
        }

        public int getLength() {
            return length;
        }

        public static ExtTag fromCode(int code) {
            for(final ExtTag tag: values()) {
                if (tag.getCode() == code)
                    return tag;
            }
            throw new IllegalStateException("Unknown tag: '" + Integer.toHexString(code) + "'");
        }

    }
}
