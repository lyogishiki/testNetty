package netty2.time;

import java.util.Date;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class UpdTimeDecoder extends ByteToMessageDecoder{

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		System.out.println("decode Buf:"+System.identityHashCode(in));
		while(in.readableBytes() >= 8) {
			UpdTime time = new UpdTime(in.readLong());
			out.add(time);
		}
		
		in.release();
	}

	
	
}
