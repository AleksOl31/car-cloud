package ru.alexanna.carcloud.service.terminal.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.alexanna.carcloud.model.Device;
import ru.alexanna.carcloud.model.Location;
import ru.alexanna.carcloud.model.Navigation;

@Slf4j
@Component
@Qualifier("GALILEO_PARSER")
public class GalileoPackageParser implements PackageParser {
    private ByteBuf responseBuf;
    private final GalileoTagDecoder galileoTagDecoder;

    public GalileoPackageParser(GalileoTagDecoder galileoTagDecoder) {
        this.galileoTagDecoder = galileoTagDecoder;
    }

    @Override
    public void parse(ByteBuf byteBuf) {
        Navigation navigation = new Navigation();
        byteBuf.resetReaderIndex();
        int firstTagInPackage = byteBuf.getByte(0);
        while (byteBuf.readerIndex() < (byteBuf.capacity() - 2)) {
            int tag = byteBuf.readByte();
            log.debug("Tag {}, first tag {}", Integer.toHexString(tag), Integer.toHexString(firstTagInPackage));
            if (tag == firstTagInPackage) {
                log.debug("Navigation data {}", navigation);
                navigation = new Navigation();
            }
            dataExtractor(tag, byteBuf, navigation);
        }
        setResponse(byteBuf);
    }

    private void dataExtractor(int tag, ByteBuf byteBuf, Navigation navigation) {
        switch (tag) {
            case 0x01:
                Integer tag01 = galileoTagDecoder.tag01(byteBuf);
                break;
            case 0x20:
                navigation.setDate(galileoTagDecoder.tag20(byteBuf));
                break;
            case 0x30:
                Location location = galileoTagDecoder.tag30(byteBuf);
                log.debug("Location {}", location);
                navigation.setLocation(location);
                break;
            default:
                System.out.println(Integer.toHexString(tag));
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
