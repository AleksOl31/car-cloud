package ru.alexanna.carcloud.service.terminal.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.UnsupportedMessageTypeException;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.util.ReferenceCountUtil;
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
    private Item connectedItem = null;
    private boolean isAuthorized = false;
    private final TerminalMessageService terminalMessageService;
    private final ItemService itemService;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("Client connected with id: {}, R: {}", ctx.channel().id(), ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            if (msg instanceof DecodedResultPacket) {
                DecodedResultPacket decodedResultPacket = (DecodedResultPacket) msg;
                if (isAuthorized) {
                    saveMonitoringPackages(decodedResultPacket.getMonitoringPackages());
                } else {
                    login(ctx, decodedResultPacket.getMonitoringPackages().get(0));
                }
                sendResponse(ctx, decodedResultPacket.getResponse());
                ReferenceCountUtil.release(decodedResultPacket);
            } else {
                throw new UnsupportedMessageTypeException("Data received on an unsupported protocol");
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    private void saveMonitoringPackages(List<MonitoringPackage> monitoringPackages) {
        terminalMessageService.saveAll(monitoringPackages, connectedItem);
    }

    private void login(ChannelHandlerContext ctx, MonitoringPackage monitoringPackage) {
        String receivedImei = monitoringPackage.getRegInfo().getImei();
        Item storedItem = itemService.findItem(receivedImei).orElseThrow();
        storedItem.setDeviceId(monitoringPackage.getRegInfo().getDeviceId());
        storedItem.setHardVer(monitoringPackage.getRegInfo().getHardVer());
        storedItem.setSoftVer(monitoringPackage.getRegInfo().getSoftVer());
        storedItem.setConnectionState(true);
        storedItem.setRemoteAddress(ctx.channel().remoteAddress().toString());
        connectedItem = itemService.save(storedItem);
        isAuthorized = true;
    }

    private void sendResponse(ChannelHandlerContext ctx, ByteBuf response) {
        ctx.write(response);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();

        if (connectedItem != null)
            log.info("Data received from device with IMEI {}, name '{}' and address {} ",
                    connectedItem.getImei(),
                    connectedItem.getName(),
                    ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        if (connectedItem != null) {
            Item storedItem = itemService.findItem(connectedItem.getImei()).orElse(connectedItem);
            log.debug("Disconnected address {}, stored address {}", connectedItem.getRemoteAddress(), storedItem.getRemoteAddress());
            if (connectedItem.getRemoteAddress().equals(storedItem.getRemoteAddress())) {
                connectedItem.setConnectionState(false);
                connectedItem.setRemoteAddress(null);
                itemService.save(connectedItem);
            }
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
        cause.printStackTrace();
    }
}
