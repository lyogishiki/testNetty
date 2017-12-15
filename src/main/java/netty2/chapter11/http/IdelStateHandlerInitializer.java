package netty2.chapter11.http;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

public class IdelStateHandlerInitializer extends
ChannelInitializer<Channel>{

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		//IdleStateHandler将在被触发时发送一个IdleStateEvent事件
//		如果超过60s没有发送或接受任何数据，那么IdleStateHandler就会使用IdelStateEvent调用fireUserEventTriggered方法
		pipeline.addLast(new 
				IdleStateHandler(0,0,60,TimeUnit.SECONDS));
		//添加一个心跳机制
		pipeline.addLast(new HeartbeatHandler());
	}

	public static class HeartbeatHandler extends ChannelInboundHandlerAdapter {
		private static final ByteBuf HEARTBEAT_SEQUENCE = 
				Unpooled.unreleasableBuffer(Unpooled.
						copiedBuffer("HEARTBEAT",StandardCharsets.UTF_8));

		
		//实现userEventTrigger方法，在超时IdleStateHandler产生IdelStateEvent事件时处理，
		//尝试发送心跳消息，如果发送失败，那么就关闭该链接
		@Override
		public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
			if(evt instanceof IdleStateEvent) {
				ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate())
					.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
			}else {	//不是IdelStateEvent事件，交给后续的ChannelInboundHandler处理
				ctx.fireUserEventTriggered(evt);
			}	
			
		}
		
		
	}
	
}
