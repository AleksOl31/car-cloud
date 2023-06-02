package ru.alexanna.carcloud.service.terminal.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.util.ReferenceCountUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.alexanna.carcloud.service.terminal.protocol.PackageParser;

@Slf4j
@AllArgsConstructor
public class GalileoServerHandler extends ChannelInboundHandlerAdapter {
    private final PackageParser packageParser;

    //    private final Logger log = LogManager.getLogger(GalileoServerHandler.class);
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.debug("Client connected with id: {}, R: {}", ctx.channel().id(), ctx.channel().remoteAddress());
//        device = new Device();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf buffer = (ByteBuf) msg;
        try {
            log.debug("Input buffer: {}", buffer);
            packageParser.parse(buffer);
            ctx.write(packageParser.getResponse());
        } finally {
            ReferenceCountUtil.release(buffer);
        }
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
