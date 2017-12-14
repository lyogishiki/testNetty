package netty2.chapter10;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

public class SafeByteToMessageDecoder extends ByteToMessageDecoder {

	private static final int MAX_FRAME_SIZE = 1024;
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		int readable = in.readableBytes();
		
		if(readable > MAX_FRAME_SIZE) {
			in.skipBytes(readable);//跳过所有可读的字节，
			throw new TooLongFrameException("Frame Too Long!");
		}
		// doSomething
		//TODO
		//FIXME
		//XXX
		//TASK
	}

	
	
}
