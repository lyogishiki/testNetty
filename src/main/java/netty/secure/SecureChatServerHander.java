package netty.secure;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;

public class SecureChatServerHander extends SimpleChannelInboundHandler<String> {

	static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.pipeline().get(SslHandler.class).handshakeFuture().addListener(
				new GenericFutureListener<Future<Channel>>() {

					@Override
					public void operationComplete(Future<Channel> future) throws Exception {
						ctx.writeAndFlush("Welcom to " + InetAddress.getLocalHost().getHostName()
								+ " secure chat server!\r\n");
						ctx.writeAndFlush("your session is protected by " + 
								ctx.pipeline().get(SslHandler.class).engine().getSession()
								.getCipherSuite() + " cipher suite.\n");
						channels.add(ctx.channel());
					}
				});
	}



	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		for(Channel c : channels){
			if(c != ctx.channel()){
				c.writeAndFlush("[" + ctx.channel().remoteAddress() + "] " + msg + '\n');
			}else{
				c.writeAndFlush("[you] " + msg + '\n');
			}
		}
		
		if("bye".equals(msg.toLowerCase())){
			ctx.close();
		}
	}



	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	
	

}
