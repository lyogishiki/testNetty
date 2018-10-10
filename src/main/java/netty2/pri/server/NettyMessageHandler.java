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
		System.out.println(ctx.alloc().getClass());
		System.out.println(msg);
		//ctx.write(msg);
	}
	
	
	/**
	 * handlerAdded 每当服务端接收到新的客户端连接时，都会调用这个方法。
	 * 也可是使用这个方法保存连接上来的客户端
	 */
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		System.out.println("-----------------NettyMessageHandler.handlerAdded()");
	}



	/**
	 * 每当连接断开时，都会调用这个方法，所以从服务器列表中去掉断开的连接的代码可以放到这里。
	 */
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		System.out.println("------------------NettyMessageHandler.handlerRemoved()");
		ServerContext.removeLoginUser(ctx.channel());
	}



	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("NettyMessageHandler.exceptionCaught()");
		cause.printStackTrace();
		ctx.close();
	}

	/**
	 * 和IdleStateHandler 配合使用。
	 */
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
