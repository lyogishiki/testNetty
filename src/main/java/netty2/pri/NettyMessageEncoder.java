package netty2.pri;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.ReferenceCountUtil;
import netty2.base.protostuff.SerializeUtils;

public class NettyMessageEncoder extends MessageToMessageEncoder<Object> {

	
	
	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
		byte[] data = SerializeUtils.serializer(msg);
		ByteBuf buf = ctx.alloc().buffer(data.length);
		buf.writeBytes(data);
		out.add(buf);
	}

}
