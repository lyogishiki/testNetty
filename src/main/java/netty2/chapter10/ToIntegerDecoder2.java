package netty2.chapter10;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

/**
 * 扩展ReplayingDecoder
 * @author Administrator
 *
 */
public class ToIntegerDecoder2 extends ReplayingDecoder<Void>{

	//传入的ByteBuf是ReplayingDecoderByteBuf
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		out.add(in.readInt());
	}

}
