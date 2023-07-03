package ru.alexanna.carcloud.service.terminal.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.UnsupportedMessageTypeException;
import io.netty.handler.timeout.ReadTimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.alexanna.carcloud.dto.DecodedResultPacket;
import ru.alexanna.carcloud.dto.MonitoringPackage;
import ru.alexanna.carcloud.entities.Item;
import ru.alexanna.carcloud.service.services.MonitoringDataService;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class ServerHandler extends ChannelInboundHandlerAdapter {
    //    private final Map<Channel, RegInfo> channelsMap = new HashMap<>();
    private final Map<Channel, Item> channelsMap = new HashMap<>();
    private boolean isAuthorized = false;
    private final MonitoringDataService monitoringDataService;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.debug("Client connected with id: {}, R: {}", ctx.channel().id(), ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof DecodedResultPacket) {
            DecodedResultPacket decodedResultPacket = (DecodedResultPacket) msg;
            if (isAuthorized) {
                //TODO Удалить эту строку
//                decodedResultPacket.getMonitoringPackages().forEach(System.out::println);
                saveMonitoringPackages(ctx, decodedResultPacket.getMonitoringPackages());
                ctx.write(decodedResultPacket.getResponse());
            } else {
                login(ctx, decodedResultPacket.getMonitoringPackages().get(0));
                ctx.write(decodedResultPacket.getResponse());
            }
        } else {
            throw new UnsupportedMessageTypeException("Data received on an unsupported protocol");
        }
    }

    private void saveMonitoringPackages(ChannelHandlerContext ctx, List<MonitoringPackage> monitoringPackages) {
        monitoringDataService.saveAll(monitoringPackages, channelsMap.get(ctx.channel()));
    }

    private void login(ChannelHandlerContext ctx, MonitoringPackage monitoringPackage) {
        String receivedImei = monitoringPackage.getRegInfo().getImei();
        Item registeredItem = monitoringDataService.findItemByImei(receivedImei).orElseThrow();
        channelsMap.put(ctx.channel(), registeredItem);
        isAuthorized = true;
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
        channelsMap.remove(ctx.channel());
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
