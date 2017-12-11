package netty.code;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

import org.msgpack.core.MessagePack;

import sun.awt.image.BytePackedRaster;

public class MsgpackDecoder extends MessageToMessageDecoder<ByteBuf> {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		final int length = msg.readableBytes();
		final byte[] array = new byte[length];
		msg.getBytes(msg.readerIndex(), array,
				0, length);
		MessagePack msgPack = new MessagePack();
		out.add(msgPack.read(array));
	}


}
