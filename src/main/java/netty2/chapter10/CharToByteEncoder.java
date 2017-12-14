package netty2.chapter10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class CharToByteEncoder extends MessageToByteEncoder<Character>{

	@Override
	protected void encode(ChannelHandlerContext ctx,
			Character msg, ByteBuf out) throws Exception {
		out.writeChar(msg.charValue());
	}

}
