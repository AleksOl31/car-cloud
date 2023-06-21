package ru.alexanna.carcloud.service.terminal.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import ru.alexanna.carcloud.service.terminal.protocol.PackageParser;

@Service
@PropertySource("classpath:connection.properties")
@Slf4j
public class BaseNettyServer implements Runnable {
    private EventLoopGroup boss;
    private EventLoopGroup worker;
    private ChannelFuture channelFuture;
    @Getter
    @Setter
//    private boolean running = false;
    @Value("${terminal.server.socket-read-timeout}")
    private int soTimeout;
    @Value("${terminal.server.galileo-port}")
    private int galileoPort;
    @Value("${terminal.server.scout-port}")
    private int scoutPort;
    private final PackageParser galileoPackageParser;
    private PackageParser scoutPackageParser;
//    private final ServerState serverState;


    public BaseNettyServer(PackageParser galileoPackageParser) {
        this.galileoPackageParser = galileoPackageParser;
        this.scoutPackageParser = galileoPackageParser;
//        this.serverState.setRunning(false);
    }

    @Override
    public void run() {
        boss = new NioEventLoopGroup(1);
        worker = new NioEventLoopGroup();
        try {
            ServerBootstrap galileoBootstrap = getServerBootstrap(boss, worker, galileoPackageParser);

            ChannelFuture galFuture = galileoBootstrap.bind(galileoPort).sync();

            // server started!
//            galFuture.channel().closeFuture().sync(); // blocking operation
            ServerBootstrap scoutBootstrap = getServerBootstrap(boss, worker, scoutPackageParser);
            ChannelFuture sctFuture = scoutBootstrap.bind(scoutPort).sync();

            // server started!
            channelFuture = galFuture;
//            setRunning(true);
            galFuture.channel().closeFuture().sync(); // blocking operation
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    private ServerBootstrap getServerBootstrap(EventLoopGroup boss, EventLoopGroup worker, PackageParser packageParser) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new LoggingHandler());
                        socketChannel.pipeline().addLast(new ReadTimeoutHandler(soTimeout));
                        socketChannel.pipeline().addLast(new GalileoPackageDecoder(packageParser));
                        socketChannel.pipeline().addLast(new ServerHandler());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        return bootstrap;
    }

    public void stop() {
        try {
            boss.shutdownGracefully().sync();
            worker.shutdownGracefully().sync();
            channelFuture.channel().closeFuture().sync();
//            setRunning(false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
