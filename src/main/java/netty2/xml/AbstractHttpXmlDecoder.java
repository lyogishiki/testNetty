package netty2.xml;

import java.nio.charset.StandardCharsets;
import java.util.List;

import com.alibaba.fastjson.JSON;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

public abstract class AbstractHttpXmlDecoder<T> extends MessageToMessageDecoder<T> {

	private final Class<?> clazz;
	
	public AbstractHttpXmlDecoder(Class<?> clazz) {
		super();
		this.clazz = clazz;
	}

	protected Object decode0(ChannelHandlerContext ctx, ByteBuf body) {
		
		String content = body.toString(StandardCharsets.UTF_8);
		System.out.println("body is : " + content);
		
		Object obj = JSON.parseObject(content,clazz);
		return obj;
	}

}
