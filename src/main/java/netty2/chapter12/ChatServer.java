package netty2.chapter12;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ImmediateEventExecutor;

public class ChatServer {
	
	//创建defaultChannelGroup，他将保存所有已经连接的WebSocket Channel
	protected final ChannelGroup channelGroup = 
			new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
	
	private final EventLoopGroup group = new NioEventLoopGroup();
	
	private Channel channel;
	
	public ChannelFuture start(InetSocketAddress address) {
		ServerBootstrap bootstrap = new ServerBootstrap();
		
		bootstrap.group(group)
			.channel(NioServerSocketChannel.class)
			.childHandler(createChannelInitializer());
		
		ChannelFuture future = bootstrap.bind(address);
		
		future.syncUninterruptibly();
		channel = future.channel();
		
		return future;
	}
	
	protected ChannelHandler createChannelInitializer() {
		return new ChatServerInitializer(channelGroup);
	}
	
	public void destory() {
		if(channel != null) {
			channel.close();
		}
		
		channelGroup.close();
		group.shutdownGracefully();
	}
	
	public static void main(String[] args) throws Exception{
		int port = 9090;
		
		final ChatServer endPoint = new ChatServer();
		
		ChannelFuture future = endPoint.start(
				new InetSocketAddress(port));
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			endPoint.destory();
		}));
		
		System.out.println(future.channel() == endPoint.channel);
		
		System.out.println(future.channel());
		System.out.println(endPoint.channel);
		future.channel().closeFuture().syncUninterruptibly();
		
	}
}
