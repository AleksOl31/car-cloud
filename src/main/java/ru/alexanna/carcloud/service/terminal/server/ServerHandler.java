package ru.alexanna.carcloud.service.terminal.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.UnsupportedMessageTypeException;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.util.ReferenceCountUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.alexanna.carcloud.dto.DecodedResultPacket;
import ru.alexanna.carcloud.service.services.ItemService;
import ru.alexanna.carcloud.service.services.TerminalMessageService;
import ru.alexanna.carcloud.service.terminal.protocol.ReceivedPacketDirector;

@Slf4j
@RequiredArgsConstructor
public class ServerHandler extends ChannelInboundHandlerAdapter {
    private final TerminalMessageService terminalMessageService;
    private final ItemService itemService;
    private ReceivedPacketDirector packetDirector;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("New client connected with channel id: {}, address: {}", ctx.channel().id(), ctx.channel().remoteAddress());
        packetDirector = new ReceivedPacketDirector(terminalMessageService, itemService);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            if (msg instanceof DecodedResultPacket) {
                DecodedResultPacket decodedResultPacket = (DecodedResultPacket) msg;
                packetDirector.packetConsumer(ctx.channel().remoteAddress(), decodedResultPacket);
                sendResponse(ctx, decodedResultPacket.getResponse());
                ReferenceCountUtil.release(decodedResultPacket);
            } else {
                throw new UnsupportedMessageTypeException("Data received on an unsupported protocol");
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    private void sendResponse(ChannelHandlerContext ctx, ByteBuf response) {
        ctx.write(response);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
        packetDirector.logReadOperation(ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        packetDirector.logout();
        log.info("Client disconnected {}", ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (cause instanceof ReadTimeoutException) {
            log.info("Socket timed out {}", ctx.channel().remoteAddress());
        } else {
            log.error("An exception occurred with the message: {}", cause.getMessage());
            ctx.close();
        }
        cause.printStackTrace();
    }
}
