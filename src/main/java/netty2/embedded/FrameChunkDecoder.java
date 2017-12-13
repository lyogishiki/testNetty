package netty2.embedded;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

public class FrameChunkDecoder
extends ByteToMessageDecoder{

	private final int maxFrameSize;
	
	public FrameChunkDecoder(int maxFrameSize) {
		super();
		this.maxFrameSize = maxFrameSize;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		int readableBytes = in.readableBytes();
		
		if(readableBytes > maxFrameSize) {
			in.clear();
			throw new TooLongFrameException();
		}
		
		ByteBuf buf = in.readBytes(readableBytes);
		out.add(buf);
	}
	
	

}
