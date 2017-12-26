package netty2.pri.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import static org.assertj.core.api.Assertions.in;

import com.sun.corba.se.spi.activation.Server;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.timeout.IdleStateEvent;
import netty2.pri.NettyMessage;
import netty2.pri.ServerContext;

@Sharable
public class NettyMessageHandler extends SimpleChannelInboundHandler<NettyMessage<Object>>{

	public static final NettyMessageHandler DEFAULT_INSTANCE = new NettyMessageHandler();
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, NettyMessage<Object> msg) throws Exception {
		System.out.println(msg);
		//ctx.write(msg);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("NettyMessageHandler.exceptionCaught()");
		cause.printStackTrace();
		ServerContext.removeLoginUser(ctx.channel());
		ctx.close();
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if(evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			System.out.println(event + ":" + event.state() + "-------------------关闭连接-------------------------");
			ServerContext.removeLoginUser(ctx.channel());
			ctx.close();
		}else {
			ctx.fireUserEventTriggered(evt);
		}
	}

	
}
