package ru.alexanna.carcloud.service.terminal.protocol.galileo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.alexanna.carcloud.model.DecodedResultPacket;
import ru.alexanna.carcloud.model.MonitoringPackage;
import ru.alexanna.carcloud.service.terminal.protocol.PackageParser;

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
                monitoringPackage.getRegInfo().setIdent(GalileoTagDecoder.tag04(byteBuf));
                break;
            case 0x10:
                monitoringPackage.getDevice().setRecordNum(GalileoTagDecoder.tag10(byteBuf));
                break;
            case 0x20:
                monitoringPackage.getNavigation().setCreatedAt(GalileoTagDecoder.tag20(byteBuf));
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
                monitoringPackage.getDevice().setTemperature(GalileoTagDecoder.tag43(byteBuf));
                break;
            case 0x50:
            case 0x51:
            case 0x52:
            case 0x53:
            case 0x54:
            case 0x55:
            case 0x56:
            case 0x57:
            case 0x78:
            case 0x79:
            case 0x7A:
            case 0x7B:
            case 0x7C:
            case 0x7D:
                monitoringPackage.getAnalogInputs().add(GalileoTagDecoder.tagAnalogInputs(byteBuf));
                break;
            case 0x60:
            case 0x61:
            case 0x62:
                monitoringPackage.getFuelSensors().add(GalileoTagDecoder.tag60_62(byteBuf));
                break;
            case 0x63:
            case 0x64:
            case 0x65:
            case 0x66:
            case 0x67:
            case 0x68:
            case 0x69:
            case 0x6A:
            case 0x6B:
            case 0x6C:
            case 0x6D:
            case 0x6E:
            case 0x6F:
                monitoringPackage.getFuelSensors().add(GalileoTagDecoder.tag63_6F(byteBuf));
                break;
            case 0x70:
            case 0x71:
            case 0x72:
            case 0x73:
            case 0x74:
            case 0x75:
            case 0x76:
            case 0x77:
                monitoringPackage.getTempSensors().add(GalileoTagDecoder.tag70_77(byteBuf));
                break;
            case 0xA0:
            case 0xA1:
            case 0xA2:
            case 0xA3:
            case 0xA4:
            case 0xA5:
            case 0xA6:
            case 0xA7:
            case 0xA8:
            case 0xA9:
            case 0xAA:
            case 0xAB:
            case 0xAC:
            case 0xAD:
            case 0xAE:
            case 0xAF:
                monitoringPackage.getCan8BitList().add(GalileoTagDecoder.tagA0_AF(byteBuf));
                break;
            case 0xB0:
            case 0xB1:
            case 0xB2:
            case 0xB3:
            case 0xB4:
            case 0xB5:
            case 0xB6:
            case 0xB7:
            case 0xB8:
            case 0xB9:
                monitoringPackage.getCan16BitList().add(GalileoTagDecoder.tagB0_B9(byteBuf));
                break;
            case 0xF0:
            case 0xF1:
            case 0xF2:
            case 0xF3:
            case 0xF4:
            case 0xF5:
            case 0xF6:
            case 0xF7:
            case 0xF8:
            case 0xF9:
                monitoringPackage.getCan32BitList().add(GalileoTagDecoder.tagF0_F9(byteBuf));
                break;
            case 0xe2:
            case 0xe3:
            case 0xe4:
            case 0xe5:
            case 0xe6:
            case 0xe7:
            case 0xe8:
            case 0xe9:
                monitoringPackage.getUserTags().add(GalileoTagDecoder.tagE2_E9(byteBuf));
                break;
            case 0xfe:
                monitoringPackage.getExtendedTags().addAll(GalileoTagDecoder.tagFE(byteBuf));
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

}
