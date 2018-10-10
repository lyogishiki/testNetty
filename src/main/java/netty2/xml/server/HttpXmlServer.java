package netty2.xml.server;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import netty.http.pojo.Order;
import netty2.xml.HttpXmlRequestDecoder;
import netty2.xml.HttpXmlResponseDecoder;
import netty2.xml.HttpXmlResponseEncoder;

public class HttpXmlServer {

	public void run(String host,int port) throws InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup(2);
		EventLoopGroup workGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<Channel>() {

						@Override
						protected void initChannel(Channel ch) throws Exception {
							ch.pipeline().addLast(new HttpRequestDecoder())
							.addLast(new HttpObjectAggregator(65536))
									.addLast(new HttpXmlRequestDecoder(Order.class))
									.addLast(new HttpResponseEncoder())
									.addLast(new HttpXmlResponseEncoder())
									.addLast(new HttpXmlServerHandler());
						}
					});
			ChannelFuture future = b.bind(new InetSocketAddress(host, port)).sync();
			future.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		String host = "127.0.0.1";
		int port = 8080;
		
		new HttpXmlServer().run(host, port);
	}
	
}
