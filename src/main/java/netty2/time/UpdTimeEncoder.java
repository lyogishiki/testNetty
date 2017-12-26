package netty2.time;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class UpdTimeEncoder extends MessageToByteEncoder<UpdTime>{

	@Override
	protected void encode(ChannelHandlerContext ctx, UpdTime msg, ByteBuf out) throws Exception {
		
		System.out.println("encode Buf:"+System.identityHashCode(out));
		out.writeLong(msg.getCurrentTime());
	}

}
