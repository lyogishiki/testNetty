package netty2.chapter11.http;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class LengthBasedInitializer extends
ChannelInitializer<Channel>{

	
	
	@Override
	protected void initChannel(Channel ch) throws Exception {
		ch.pipeline()
		//长度编码偏移量0，
			.addLast(new LengthFieldBasedFrameDecoder(64*1024, 0, 8))
			.addLast(new FrameHandler());
		
	}

	
	public static final class FrameHandler extends SimpleChannelInboundHandler<ByteBuf>{

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
			System.out.println(msg);
		}
		
	}
}
