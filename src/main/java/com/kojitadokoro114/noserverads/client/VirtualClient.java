package com.kojitadokoro114.noserverads.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

@SuppressWarnings("rawtypes")
public class VirtualClient {

    private static final EventLoopGroup GROUP = new NioEventLoopGroup();
    private static final Bootstrap BOOTSTRAP = new Bootstrap();

    static {
        BOOTSTRAP.group(GROUP)
            .channel(NioSocketChannel.class)
            .handler(new ChannelInitializer() {
                @Override
                protected void initChannel(Channel ch) {
                    ch.pipeline().addLast(new SimpleDecoder());
                }
            });
    }

    public static void tryPing(String host, int port, Runnable onConnect) {
        BOOTSTRAP.clone()
            .handler(new ChannelInitializer() {
                @Override
                protected void initChannel(Channel ch) {
                    ch.pipeline()
                        .addLast(new SimpleDecoder())
                        .addLast(new PingHandler(host, port, 760, onConnect));
                }
            })
            .connect(host, port);
    }

    public static void shutdown() {
        GROUP.shutdownGracefully();
    }
}
