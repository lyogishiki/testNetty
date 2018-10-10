package netty2.xml;

import com.alibaba.fastjson.JSON;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

public abstract class AbstractHttpXmlEncoder<T> extends MessageToMessageEncoder<T> {

	protected ByteBuf encode0(ChannelHandlerContext ctx, 
			Object body) {
//		JSON.toJSONBytes(object, features)
//		ObjectMappr
		byte[] data = JSON.toJSONBytes(body);
		ByteBuf buf = ctx.alloc().buffer(data.length);
		buf.writeBytes(data);
		return buf;
	}

}
