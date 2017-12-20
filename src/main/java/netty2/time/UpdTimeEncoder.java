package netty2.time;

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
