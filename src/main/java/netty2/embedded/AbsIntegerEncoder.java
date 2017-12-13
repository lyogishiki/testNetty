package netty2.embedded;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

public class AbsIntegerEncoder 
extends MessageToMessageEncoder<ByteBuf>{

	@Override
	protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		while(msg.readableBytes() >= 4) {	//检查是否有足够的字节用来编码
			int value = Math.abs(msg.readInt());	//从输入的ByteBuf中读取下一个整数
			out.add(value);		//将该整数写入到编码消息的list中
		}
		
	}

}
