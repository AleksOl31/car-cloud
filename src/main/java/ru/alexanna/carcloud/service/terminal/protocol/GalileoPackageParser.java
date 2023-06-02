package ru.alexanna.carcloud.service.terminal.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.alexanna.carcloud.model.MonitoringPackage;
import ru.alexanna.carcloud.model.Navigation;

@Slf4j
@Component
@Qualifier("GALILEO_PARSER")
public class GalileoPackageParser implements PackageParser {
    private ByteBuf responseBuf;
    private final GalileoTagDecoder galileoTagDecoder;
    private MonitoringPackage monitoringPackage;

    public GalileoPackageParser(GalileoTagDecoder galileoTagDecoder) {
        this.galileoTagDecoder = galileoTagDecoder;
        monitoringPackage = new MonitoringPackage();
    }

    @Override
    public void parse(ByteBuf byteBuf) {
        Navigation navigation = null;
        byteBuf.resetReaderIndex();
        int firstTagInPackage = byteBuf.getByte(0);
        while (byteBuf.readerIndex() < (byteBuf.capacity() - 2)) {
            int tag = byteBuf.readUnsignedByte();
//            log.debug("Tag {}, first tag {}", Integer.toHexString(tag), Integer.toHexString(firstTagInPackage));
            if (tag == firstTagInPackage) {
                log.debug("Navigation data {}", navigation);
                navigation = new Navigation();
            }
            dataExtractor(tag, byteBuf);
        }
        setResponse(byteBuf);
    }

    private void dataExtractor(int tag, ByteBuf byteBuf) {
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
                navigation.setDate(galileoTagDecoder.tag20(byteBuf));
                break;
            case 0x30:
                navigation.setLocation(galileoTagDecoder.tag30(byteBuf));
                break;

            case 0xfe:
//                galileoTagDecoder.tagFE(byteBuf);
                GalileoTagDecoder.tagFE(byteBuf);
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
