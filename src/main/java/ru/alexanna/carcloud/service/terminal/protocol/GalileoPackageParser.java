package ru.alexanna.carcloud.service.terminal.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.alexanna.carcloud.model.MonitoringData;
import ru.alexanna.carcloud.model.MonitoringPackage;

@Slf4j
@Component
@Qualifier("GALILEO_PARSER")
@NoArgsConstructor
public class GalileoPackageParser implements PackageParser {
    private ByteBuf responseBuf;
    private MonitoringPackage monitoringPackage;

    @Override
    public void parse(ByteBuf byteBuf) {
//        monitoringPackage = new MonitoringPackage();
/*        MonitoringData monitoringData = null;
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
        setResponse(byteBuf);*/

        // FIXME: 04.06.2023 циклы работают неправильно. Подумать о переделке модели
        byteBuf.resetReaderIndex();
        int firstTagInPackage = byteBuf.getByte(0);
        monitoringPackage = new MonitoringPackage();

        while (byteBuf.readerIndex() < (byteBuf.capacity() - 2)) {
            MonitoringData monitoringData = new MonitoringData();
            int tag = byteBuf.readUnsignedByte();
            do {
                dataExtractor(tag, byteBuf,monitoringData);
                tag = byteBuf.readUnsignedByte();
            } while (tag != firstTagInPackage && byteBuf.readerIndex() < (byteBuf.capacity() - 2));
            monitoringPackage.add(monitoringData);
        }
        log.debug("{}", monitoringPackage);
        setResponse(byteBuf);
    }

    private void dataExtractor(int tag, ByteBuf byteBuf, MonitoringData monitoringData) {
        switch (tag) {
            case 0x01:
                monitoringPackage.getRegInfo().setHardVer(GalileoTagDecoder.tag01(byteBuf));
                break;
            case 0x02:
                monitoringPackage.getRegInfo().setSoftVer(GalileoTagDecoder.tag02(byteBuf));
                break;
            case 0x03:
                monitoringPackage.getRegInfo().setImei(GalileoTagDecoder.tag03(byteBuf));
                break;
            case 0x04:
                monitoringPackage.getRegInfo().setId(GalileoTagDecoder.tag04(byteBuf));
                break;
            case 0x10:
                monitoringPackage.getDevice().setRecordNum(GalileoTagDecoder.tag10(byteBuf));
                break;
            case 0x20:
                monitoringData.getNavigation().setDate(GalileoTagDecoder.tag20(byteBuf));
                break;
            case 0x30:
                monitoringData.getNavigation().setLocation(GalileoTagDecoder.tag30(byteBuf));
                break;
            case 0x33:
                GalileoTagDecoder.MotionInfo motionInfo = GalileoTagDecoder.tag33(byteBuf);
                monitoringData.getNavigation().setSpeed(motionInfo.getSpeed());
                monitoringData.getNavigation().setCourse(motionInfo.getCourse());
                break;
            case 0xfe:
                GalileoTagDecoder.tagFE(byteBuf);
                break;
            default:
                byteBuf.skipBytes(GalileoTag.length(tag));
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
