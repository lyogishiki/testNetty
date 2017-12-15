package netty2.chapter11.http;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.LineBasedFrameDecoder;

public class LineBasedHandlerInitializer extends
ChannelInitializer<Channel>{

	
	
	@Override
	protected void initChannel(Channel ch) throws Exception {
		ch.pipeline().addLast(
				//一行最长64K
				new LineBasedFrameDecoder(64*1024))
			.addLast(new FrameHandler());
	}
	
	public static final class FrameHandler extends SimpleChannelInboundHandler<ByteBuf>{

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
			System.out.println(msg);
		}
		
	}
	
	

}
