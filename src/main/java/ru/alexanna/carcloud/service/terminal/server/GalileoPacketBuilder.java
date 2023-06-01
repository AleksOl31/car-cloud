package ru.alexanna.carcloud.service.terminal.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.handler.codec.UnsupportedMessageTypeException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class GalileoPacketBuilder extends ReplayingDecoder<Void> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readByte() == 0x01) {
            int packLength = (byteBuf.readShortLE() & 0x7FFF) + 5;
            byteBuf.resetReaderIndex();
            ByteBuf dataBuf = byteBuf.readBytes(packLength).copy(3, packLength - 3);
            list.add(dataBuf);
        } else {
            throw new UnsupportedMessageTypeException("Data received on an unsupported protocol");
        }
    }
}
