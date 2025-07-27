// Powered by Mojang Studio
package com.kojitadokoro114.noserverads.client.util;

import io.netty.buffer.ByteBuf;

public class VarInt {

    public static boolean hasContinuationBit(byte data) {
        return (data & 128) == 128;
    }

    public static int read(ByteBuf buffer) {
        int i = 0;
        int i1 = 0;
        byte _byte;
        do {
            _byte = buffer.readByte();
            i |= (_byte & 127) << i1++ * 7;
            if (i1 > 5) {
                throw new RuntimeException("VarInt too big");
            }
        } while (hasContinuationBit(_byte));

        return i;
    }
    public static void write(ByteBuf buffer, int value) {
        if ((value & -128) == 0) {
            buffer.writeByte(value);
        } else if ((value & -16384) == 0) {
            int w = (value & 127 | 128) << 8 | value >>> 7;
            buffer.writeShort(w);
        } else {
            writeOld(buffer, value);
        }
    }
    public static void writeOld(ByteBuf buffer, int value) {
        while ((value & -128) != 0) {
            buffer.writeByte(value & 127 | 128);
            value >>>= 7;
        }
        buffer.writeByte(value);
    }
}