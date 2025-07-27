package com.kojitadokoro114.noserverads.client;

import com.kojitadokoro114.noserverads.client.util.VarInt;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

class SimpleDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        in.markReaderIndex();
        if (!in.isReadable()) {
            return;
        }
        try {
            int packetLength = VarInt.read(in);
            if (in.readableBytes() < packetLength) {
                in.resetReaderIndex();
                return;
            }
            ByteBuf frame = in.readRetainedSlice(packetLength);
            out.add(frame);
        } catch (IndexOutOfBoundsException e) {
            in.resetReaderIndex();
        }
    }
}