package ru.alexanna.carcloud.service.terminal.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.handler.codec.UnsupportedMessageTypeException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.alexanna.carcloud.service.terminal.protocol.PackageParser;

import java.util.List;

@Slf4j
@AllArgsConstructor
public class GalileoPackageDecoder extends ReplayingDecoder<Void> {
    private final PackageParser packageParser;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        final int HEADER_LENGTH = 1;
        final int DATA_SIZE_LENGTH = 2;
        final int CRC_LENGTH = 2;
        // FIXME: 06.06.2023 Добавлено в виде опции JVM: -Dio.netty.leakDetectionLevel=advanced
//        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.ADVANCED);
        byte headerValue = byteBuf.readByte();
        int dataSize = byteBuf.readShortLE() & 0x7FFF;
        int fullPackSize = dataSize + HEADER_LENGTH + DATA_SIZE_LENGTH + CRC_LENGTH;
        byteBuf.resetReaderIndex();
        if (headerValue == 0x01 && fullPackSize <= 1000) {
            ByteBuf dataBuf = byteBuf.readBytes(fullPackSize).slice(3, fullPackSize - 3);
            list.add(packageParser.parse(dataBuf));
        } else {
            channelHandlerContext.pipeline().remove(this);
            throw new UnsupportedMessageTypeException("Data received on an unsupported protocol");
        }
    }
}
