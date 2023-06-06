package ru.alexanna.carcloud.service.terminal.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.UnsupportedMessageTypeException;
import io.netty.handler.timeout.ReadTimeoutException;
import lombok.extern.slf4j.Slf4j;
import ru.alexanna.carcloud.model.DecodedResultPacket;
import ru.alexanna.carcloud.model.RegInfo;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ServerHandler extends ChannelInboundHandlerAdapter {
    private final Map<ChannelId, RegInfo> channelsMap = new HashMap<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.debug("Client connected with id: {}, R: {}", ctx.channel().id(), ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof DecodedResultPacket) {
            DecodedResultPacket decodedResultPacket = (DecodedResultPacket) msg;
            DecodedResultPacket updatedDecodedResultPacket = updateRegInfo(ctx, decodedResultPacket);
            log.debug("Input buffer: {}", updatedDecodedResultPacket.getMonitoringPackages());
            ctx.write(updatedDecodedResultPacket.getResponse());
        } else
            throw new UnsupportedMessageTypeException("Data received on an unsupported protocol");
    }

    private DecodedResultPacket updateRegInfo(ChannelHandlerContext ctx, DecodedResultPacket decodedResultPacket) {
        if (channelsMap.get(ctx.channel().id()) == null) {
            channelsMap.put(ctx.channel().id(), decodedResultPacket.getMonitoringPackages().get(0).getRegInfo());
        } else {
            decodedResultPacket.getMonitoringPackages().forEach((monitoringPackage) -> {
                monitoringPackage.setRegInfo(channelsMap.get(ctx.channel().id()));
            });
        }
        return decodedResultPacket;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
        System.out.println("--------------------------------------------------------------------" +
                "----------------------------------------------------------------------------");
        System.out.println();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.debug("Client disconnected {}", ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (cause instanceof ReadTimeoutException) {
            log.debug("Socket timed out {}", ctx.channel().remoteAddress());
        } else {
            log.error("An exception occurred with the message: {}", cause.getMessage());
            ctx.close();
        }
        cause.printStackTrace();
    }
}
