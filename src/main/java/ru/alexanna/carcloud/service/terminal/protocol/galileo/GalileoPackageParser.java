package ru.alexanna.carcloud.service.terminal.protocol.galileo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.alexanna.carcloud.dto.DecodedResultPacket;
import ru.alexanna.carcloud.dto.MonitoringPackage;
import ru.alexanna.carcloud.service.terminal.protocol.PackageParser;
import ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.TagGroupDecoder;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Qualifier("GALILEO_PARSER")
public class GalileoPackageParser implements PackageParser {
    @Override
    public DecodedResultPacket parse(ByteBuf byteBuf) {
        try {
            List<MonitoringPackage> monitoringPackages = new ArrayList<>();
            byteBuf.resetReaderIndex();
            int firstTagInPackage = byteBuf.getByte(0);
            MonitoringPackage monitoringPackage = new MonitoringPackage();
            int packageNumber = 0;
            while (byteBuf.readerIndex() < (byteBuf.capacity() - 2)) {
                int tag = byteBuf.readUnsignedByte();
                packageNumber++;
                monitoringPackage = decode(tag, byteBuf, monitoringPackage);
                int nextTag = byteBuf.getUnsignedByte(byteBuf.readerIndex());
                if (nextTag == firstTagInPackage && packageNumber != 1 && byteBuf.readerIndex() < (byteBuf.capacity() - 2)) {
                    monitoringPackages.add(monitoringPackage);
                    monitoringPackage = new MonitoringPackage();
                }
            }
            monitoringPackages.add(monitoringPackage);
            ByteBuf responseBuf = createResponse(byteBuf);
            return new DecodedResultPacket(monitoringPackages, responseBuf);
        } finally {
            byteBuf.release();
        }
    }

    private MonitoringPackage decode(int tagCode, ByteBuf byteBuf, MonitoringPackage monitoringPackage) {
        TagGroupDecoder tagGroupDecoder = TagGroupDecoder.create(tagCode, monitoringPackage);
        return tagGroupDecoder.decode(byteBuf);
    }

    private ByteBuf createResponse(ByteBuf byteBuf) {
        ByteBuf responseBuf = Unpooled.buffer(3, 3);
        responseBuf.writeByte(0x02);
        responseBuf.writeBytes(byteBuf);
        return responseBuf;
    }

}
