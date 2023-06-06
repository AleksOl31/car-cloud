package ru.alexanna.carcloud.service.terminal.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.handler.codec.UnsupportedMessageTypeException;
import io.netty.util.ResourceLeakDetector;
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
        if (byteBuf.readByte() == 0x01) {
            int packLength = (byteBuf.readShortLE() & 0x7FFF) + 5;
            if (packLength <= 1000) {
                byteBuf.resetReaderIndex();
                ByteBuf dataBuf = byteBuf.readBytes(packLength).copy(3, packLength - 3);
                list.add(packageParser.parse(dataBuf));
                // FIXME: 06.06.2023 Протестировать
                ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.ADVANCED);
            }
        } else {
            list.add(byteBuf);
            throw new UnsupportedMessageTypeException("Data received on an unsupported protocol");
        }
    }
}
