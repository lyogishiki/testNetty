package netty2.chapter13.monitor;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class LogEventMonitor {

	private final EventLoopGroup group;
	private final Bootstrap bootstrap;
	
	
	public LogEventMonitor(InetSocketAddress address) {
		super();
		group = new NioEventLoopGroup();
		bootstrap = new Bootstrap();
		
		bootstrap.group(group)
			.channel(NioDatagramChannel.class)
			.option(ChannelOption.SO_BROADCAST, true)
			.handler(new ChannelInitializer<Channel>() {

				@Override
				protected void initChannel(Channel ch) throws Exception {
					ch.pipeline()
						.addLast(new LogEventDecoder())
						.addLast(new LogEventHandler());
				}
			}).localAddress(address);
	}
	
	public Channel bind() {
		return bootstrap.bind().syncUninterruptibly()
				.channel();
		
	}
	
	public void stop() {
		group.shutdownGracefully();
	}
	
	public static void main(String[] args) {
		
		int port = 9090;
		
		LogEventMonitor monitor = new LogEventMonitor(new 
				InetSocketAddress(port));
		
		try {
			Channel channel = monitor.bind();
			System.out.println("LogEventMonitor running");
			channel.closeFuture().syncUninterruptibly();
//			channel.close();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			monitor.stop();
		}
		
	}
}
