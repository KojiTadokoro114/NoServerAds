package com.kojitadokoro114.noserverads.client;

import com.kojitadokoro114.noserverads.client.util.StringIOUtil;
import com.kojitadokoro114.noserverads.client.util.VarInt;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

class PingHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private final String host;
    private final int port;
    private final int protocol;
    private final Runnable onConnect;

    public PingHandler(String host, int port, int protocol, Runnable onConnect) {
        this.host = host;
        this.port = port;
        this.protocol = protocol;
        this.onConnect = onConnect;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ByteBuf temp = Unpooled.buffer();
        VarInt.write(temp, 0x00);
        VarInt.write(temp, protocol);
        StringIOUtil.write(temp, host, 32767);
        temp.writeShort(port);
        VarInt.write(temp, 0x01);

        ByteBuf handshake = Unpooled.buffer();
        VarInt.write(handshake, temp.readableBytes());
        handshake.writeBytes(temp);
        temp.release();
        ctx.writeAndFlush(handshake);

        ByteBuf request = Unpooled.buffer();
        request.writeByte(0x01);
        request.writeByte(0x00);
        ctx.writeAndFlush(request);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) {
        try {
            int packetId = VarInt.read(in);
            if (packetId != 0x00) {
                throw new IllegalStateException("Invalid packet ID: " + packetId);
            }
            String response = StringIOUtil.read(in, 32767);
            if (response.startsWith("{\"version\":")) {
                onConnect.run();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}