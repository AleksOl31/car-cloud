package ru.alexanna.carcloud.service.terminal.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.alexanna.carcloud.model.MonitoringData;
import ru.alexanna.carcloud.model.MonitoringPackage;

@Slf4j
@Component
@Qualifier("GALILEO_PARSER")
public class GalileoPackageParser implements PackageParser {
    private ByteBuf responseBuf;
    private final GalileoTagDecoder galileoTagDecoder;
    private MonitoringPackage monitoringPackage;

    public GalileoPackageParser(GalileoTagDecoder galileoTagDecoder) {
        this.galileoTagDecoder = galileoTagDecoder;
    }

    @Override
    public void parse(ByteBuf byteBuf) {
        monitoringPackage = new MonitoringPackage();
        MonitoringData monitoringData = null;
        byteBuf.resetReaderIndex();
        int firstTagInPackage = byteBuf.getByte(0);
        while (byteBuf.readerIndex() < (byteBuf.capacity() - 2)) {
            int tag = byteBuf.readUnsignedByte();
            if (tag == firstTagInPackage) {
                monitoringPackage.add(monitoringData);
                monitoringData = new MonitoringData();
            }
            dataExtractor(tag, byteBuf, monitoringData);
        }
        log.debug("{}", monitoringPackage);
        setResponse(byteBuf);
    }

    private void dataExtractor(int tag, ByteBuf byteBuf, MonitoringData monitoringData) {
        switch (tag) {
            case 0x01:
                monitoringPackage.getDevice().setHardVer(galileoTagDecoder.tag01(byteBuf));
                break;
            case 0x02:
                monitoringPackage.getDevice().setSoftVer(galileoTagDecoder.tag02(byteBuf));
                break;
            case 0x03:
                monitoringPackage.getDevice().setImei(galileoTagDecoder.tag03(byteBuf));
                break;
            case 0x04:
                monitoringPackage.getDevice().setId(galileoTagDecoder.tag04(byteBuf));
                break;
            case 0x10:
                monitoringPackage.getDevice().setRecordNum(galileoTagDecoder.tag10(byteBuf));
                break;
            case 0x20:
                monitoringData.getNavigation().setDate(galileoTagDecoder.tag20(byteBuf));
                break;
            case 0x30:
                monitoringData.getNavigation().setLocation(galileoTagDecoder.tag30(byteBuf));
                break;
            case 0x33:
                GalileoTagDecoder.MotionInfo motionInfo = galileoTagDecoder.tag33(byteBuf);
                monitoringData.getNavigation().setSpeed(motionInfo.getSpeed());
                monitoringData.getNavigation().setCourse(motionInfo.getCourse());
                break;
            case 0xfe:
                galileoTagDecoder.tagFE(byteBuf);
                break;
            default:
                byteBuf.skipBytes(galileoTagDecoder.length(tag));
        }
    }

    private void setResponse(ByteBuf byteBuf) {
        responseBuf = Unpooled.buffer(3, 3);
        responseBuf.writeByte(0x02);
        responseBuf.writeBytes(byteBuf);
    }

    @Override
    public ByteBuf getResponse() {
        return responseBuf;
    }
}
