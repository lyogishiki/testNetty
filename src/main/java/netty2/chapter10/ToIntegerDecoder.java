package netty2.chapter10;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 虽然可以完成功能，在调用readInt前不得不验证所输入的数据是否足够，
 * 可以使用ReplayingDecoder，以少量的开销解除了每次的验证。
 * @author Administrator
 *
 */
public class ToIntegerDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if(in.readableBytes() >= 4) {
			out.add(in.readInt());
		}
	}

}
