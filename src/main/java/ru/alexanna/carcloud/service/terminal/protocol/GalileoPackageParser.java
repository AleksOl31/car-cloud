package ru.alexanna.carcloud.service.terminal.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.alexanna.carcloud.model.DecodedResultPacket;
import ru.alexanna.carcloud.model.MonitoringPackage;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Qualifier("GALILEO_PARSER")
@NoArgsConstructor
public class GalileoPackageParser implements PackageParser {
//    private ByteBuf responseBuf;
    private MonitoringPackage monitoringPackage;

    @Override
    public DecodedResultPacket parse(ByteBuf byteBuf) {
        List<MonitoringPackage> monitoringPackages = new ArrayList<>();
        byteBuf.resetReaderIndex();
        int firstTagInPackage = byteBuf.getByte(0);
        monitoringPackage = new MonitoringPackage();
        int packetNumber = 0;
        while (byteBuf.readerIndex() < (byteBuf.capacity() - 2)) {
            int tag = byteBuf.readUnsignedByte();
            packetNumber++;
            dataExtractor(tag, byteBuf);
            int nextTag = byteBuf.getUnsignedByte(byteBuf.readerIndex());
            if (nextTag == firstTagInPackage && packetNumber != 1 && byteBuf.readerIndex() < (byteBuf.capacity() - 2)) {
                monitoringPackages.add(monitoringPackage);
                monitoringPackage = new MonitoringPackage();
            }
        }
        monitoringPackages.add(monitoringPackage);
        ByteBuf responseBuf = createResponse(byteBuf);
        byteBuf.release();
        return new DecodedResultPacket(monitoringPackages, responseBuf);
    }

    private void dataExtractor(int tag, ByteBuf byteBuf) {
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
                monitoringPackage.getNavigation().setDate(GalileoTagDecoder.tag20(byteBuf));
                break;
            case 0x30:
                monitoringPackage.getNavigation().setLocation(GalileoTagDecoder.tag30(byteBuf));
                break;
            case 0x33:
                GalileoTagDecoder.MotionInfo motionInfo = GalileoTagDecoder.tag33(byteBuf);
                monitoringPackage.getNavigation().setSpeed(motionInfo.getSpeed());
                monitoringPackage.getNavigation().setCourse(motionInfo.getCourse());
                break;
            case 0x34:
                monitoringPackage.getNavigation().setHeight(GalileoTagDecoder.tag34(byteBuf));
                break;
            case 0x35:
                int dop = GalileoTagDecoder.tag35(byteBuf);
                if (monitoringPackage.getNavigation().getLocation().getCorrect()) {
                    if (monitoringPackage.getNavigation().getLocation().getCorrectness() == 0)
                        monitoringPackage.getNavigation().setHdop(dop / 10.);
                    else if (monitoringPackage.getNavigation().getLocation().getCorrectness() == 2)
                        monitoringPackage.getNavigation().setHdop(dop * 10.);
                }
                break;
            case 0x40:
                monitoringPackage.getDevice().setStatus(GalileoTagDecoder.tag40(byteBuf));
                break;
            case 0x41:
                monitoringPackage.getDevice().setSupplyVol(GalileoTagDecoder.tag41(byteBuf));
                break;
            case 0x42:
                monitoringPackage.getDevice().setBatteryVol(GalileoTagDecoder.tag42(byteBuf));
                break;
            case 0x43:
                monitoringPackage.getDevice().setTemp(GalileoTagDecoder.tag43(byteBuf));
                break;
            case 0xfe:
                GalileoTagDecoder.tagFE(byteBuf);
                break;
            default:
                byteBuf.skipBytes(GalileoTag.length(tag));
        }
    }

    private ByteBuf createResponse(ByteBuf byteBuf) {
        ByteBuf responseBuf = Unpooled.buffer(3, 3);
        responseBuf.writeByte(0x02);
        responseBuf.writeBytes(byteBuf);
        return responseBuf;
    }

    /*@Override
    public ByteBuf getResponse() {
        return responseBuf;
    }*/
}
