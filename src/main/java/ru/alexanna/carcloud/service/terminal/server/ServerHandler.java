package ru.alexanna.carcloud.service.terminal.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.UnsupportedMessageTypeException;
import io.netty.handler.timeout.ReadTimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.alexanna.carcloud.dto.DecodedResultPacket;
import ru.alexanna.carcloud.dto.MonitoringPackage;
import ru.alexanna.carcloud.entities.Item;
import ru.alexanna.carcloud.service.services.ItemService;
import ru.alexanna.carcloud.service.services.TerminalMessageService;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class ServerHandler extends ChannelInboundHandlerAdapter {
    private final Map<Channel, Item> channelsMap = new HashMap<>();
    private boolean isAuthorized = false;
    private final TerminalMessageService terminalMessageService;
    private final ItemService itemService;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("Client connected with id: {}, R: {}", ctx.channel().id(), ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof DecodedResultPacket) {
            DecodedResultPacket decodedResultPacket = (DecodedResultPacket) msg;
            if (isAuthorized) {
                saveMonitoringPackages(ctx, decodedResultPacket.getMonitoringPackages());
            } else {
                login(ctx, decodedResultPacket.getMonitoringPackages().get(0));
            }
            sendResponse(ctx, decodedResultPacket.getResponse());
        } else {
            throw new UnsupportedMessageTypeException("Data received on an unsupported protocol");
        }
    }

    private void saveMonitoringPackages(ChannelHandlerContext ctx, List<MonitoringPackage> monitoringPackages) {
        terminalMessageService.saveAll(monitoringPackages, channelsMap.get(ctx.channel()));
    }

    private void login(ChannelHandlerContext ctx, MonitoringPackage monitoringPackage) {
        String receivedImei = monitoringPackage.getRegInfo().getImei();
        Item registeredItem = itemService.findItem(receivedImei).orElseThrow();
        registeredItem.setDeviceId(monitoringPackage.getRegInfo().getDeviceId());
        registeredItem.setHardVer(monitoringPackage.getRegInfo().getHardVer());
        registeredItem.setSoftVer(monitoringPackage.getRegInfo().getSoftVer());
        registeredItem.setConnectionState(true);
        Item savedItem = itemService.save(registeredItem);
        channelsMap.put(ctx.channel(), savedItem);
        isAuthorized = true;
    }

    private void sendResponse(ChannelHandlerContext ctx, ByteBuf response) {
        ctx.write(response);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
//        System.out.println("--------------------------------------------------------------------" +
//                "----------------------------------------------------------------------------");
//        System.out.println();
        if (channelsMap.get(ctx.channel()) != null)
            log.info("Data received from device with IMEI {} and address {} ", channelsMap.get(ctx.channel()).getImei(), ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Item item = channelsMap.remove(ctx.channel());
        if (item != null) {
            item.setConnectionState(false);
            itemService.save(item);
        }
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
//        cause.printStackTrace();
    }
}
