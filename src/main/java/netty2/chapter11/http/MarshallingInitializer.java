package netty2.chapter11.http;

import java.io.Serializable;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

public class MarshallingInitializer extends ChannelInitializer<Channel>{

	private final MarshallerProvider marshallerProvider;
	private final UnmarshallerProvider unmarshallerProvider;
	
	public MarshallingInitializer(MarshallerProvider marshallerProvider, UnmarshallerProvider unmarshallerProvider) {
		super();
		this.marshallerProvider = marshallerProvider;
		this.unmarshallerProvider = unmarshallerProvider;
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ch.pipeline().addLast(
				//marshallingDecoder 将Bytebuf解码为POJO
				new MarshallingDecoder(unmarshallerProvider))
		//	MarshallingEncoder 将POJO 编码为ByteBuf
			.addLast(new MarshallingEncoder(marshallerProvider))
			.addLast(new ObjectHandler());
	}
	
	public static final class ObjectHandler extends SimpleChannelInboundHandler<Serializable>{

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, Serializable msg) throws Exception {
			// TASK Auto-generated method stub
			
			System.out.println(msg);
		}
		
	}
	
	
	

}
