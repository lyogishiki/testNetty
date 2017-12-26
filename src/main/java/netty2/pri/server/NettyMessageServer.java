package netty2.pri.server;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import netty.Consts;

public class NettyMessageServer {

	private final String host;
	private final int port;
	
	private final EventLoopGroup bossGroup;
	private final EventLoopGroup workGroup;
	
	public NettyMessageServer(String host, int port) {
		this(host, port, new NioEventLoopGroup(), new NioEventLoopGroup());
	}

	public NettyMessageServer(String host, int port, EventLoopGroup bossGroup, EventLoopGroup workGroup) {
		super();
		this.host = host;
		this.port = port;
		this.bossGroup = bossGroup;
		this.workGroup = workGroup;
	}
	
	public void start() throws Exception {
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		
		serverBootstrap.
			group(bossGroup, workGroup)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 128)
			.localAddress(new InetSocketAddress(host, port))
			.childHandler(new NettyMessageServerInitializer());		
		
		ChannelFuture future = serverBootstrap.bind().sync();
		
		System.out.println("Channel Server startup on " + host + ":" + port);
		//阻塞等待有关闭实践，也就是调用了close方法。
		future.channel().closeFuture().sync();
	}
	
	public void stop() {
		bossGroup.shutdownGracefully();
		workGroup.shutdownGracefully();
	}
	
	public static void main(String[] args) {
		NettyMessageServer server = new NettyMessageServer(Consts.HOST	, Consts.PORT);
		try {
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			server.stop();
		}
	}
}
