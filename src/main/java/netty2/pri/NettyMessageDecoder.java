package netty2.pri;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import netty2.base.protostuff.SerializeUtils;

public class NettyMessageDecoder<T> extends MessageToMessageDecoder<ByteBuf>{

	private final Class<T> clazz;
	
	public NettyMessageDecoder(Class<T> clazz) {
		super();
		this.clazz = clazz;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		// MYTASK Auto-generated method stub
		//不够四位 肯定是没有接收完,加了LengthFieldBasedFrameDecoder之后，这些是不需要的，因为已经处理了
		/*if(in.readableBytes() < 4) {
			return;
		}
		in.markReaderIndex();
		if(in.readableBytes() < length) {
			in.resetReaderIndex();
			return;
		}*/
		
		/*int length = in.readInt();
		
		byte[] buf = new byte[length];
		
		in.readBytes(buf);
		
		ByteArrayInputStream bis = new ByteArrayInputStream(buf);
		ObjectInputStream ois = new ObjectInputStream(bis);
		
		NettyMessage<? extends Object> nettyMessage = (NettyMessage<? extends Object>) ois.readObject();
		out.add(nettyMessage);*/
//MYTASK 优化，可以在有使用的heatBuf的情况下判断hashArray，如果true,直接调用in.array() 返回byte数组
		System.out.println("=======hasArray : " + in.hasArray() );
		
		byte[] data = new byte[in.readableBytes()];
		in.readBytes(data);
		out.add(SerializeUtils.deSerialize(data, clazz));
	}

}
