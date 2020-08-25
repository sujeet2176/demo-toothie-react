package com.demotoothie.comm;

import com.xuhao.didi.core.iocore.interfaces.IPulseSendable;

import java.nio.ByteBuffer;

public class PulseData implements IPulseSendable {

    @Override
    public byte[] parse() {
        // length(8) = header(4) + type(id:1) + sessionId(1) + reserved(2)
        int length = 8;
        ByteBuffer bb = ByteBuffer.allocate(length);
        // header
        byte[] header = new byte[4];
        header[0] = 0;
        header[1] = 0;
        header[2] = 0;
        header[3] = 4;
        bb.put(header);
        // type
        byte[] type = new byte[4];
        type[0] = TCPMessage.MSG_ID_HEARTBEAT;
        type[1] = 0;
        type[2] = 0;
        type[3] = 0;
        bb.put(type);

        return bb.array();
    }
}
