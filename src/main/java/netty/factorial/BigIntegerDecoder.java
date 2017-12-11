package netty.factorial;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;

public class BigIntegerDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if(in.readableBytes() < 5){
			return;
		}
		
		in.markReaderIndex();
		
		int magicNumber = in.readUnsignedByte();
		if(magicNumber != 'F'){
			in.resetReaderIndex();
			throw new CorruptedFrameException("Invalid magic number: " + magicNumber);
		}
		
		int dataLength = in.readInt();
		if(in.readableBytes() < dataLength){
			in.resetReaderIndex();
			return;
		}
		
		byte[] decoded = new byte[dataLength];
		in.readBytes(decoded);
		BigInteger v = new BigInteger(decoded);
		out.add(v);
	}

}
