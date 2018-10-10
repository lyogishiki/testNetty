package netty2.xml.client;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import netty.http.pojo.Order;
import netty2.xml.HttpXmlRequestEncoder;
import netty2.xml.HttpXmlResponseDecoder;

public class HttpXmlClient {

	public void connect(int port) throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup(1);
		
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
					//			.option(ChannelOption.TCP_NODELAY, value)
					.handler(new ChannelInitializer<Channel>() {

						@Override
						protected void initChannel(Channel ch) throws Exception {
							ch.pipeline().addLast("http-decoder", new HttpResponseDecoder())
									.addLast("http-aggregator", new HttpObjectAggregator(65536))
									.addLast("json-decoder", new HttpXmlResponseDecoder(Order.class))
									.addLast("http-encoder", new HttpRequestEncoder())
									.addLast("json-encoder", new HttpXmlRequestEncoder())
									.addLast(new httpXmlClientHandler());
						}
					});
			ChannelFuture f = b.connect(new InetSocketAddress("127.0.0.1", port)).sync();
			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		int port = 8080;
		
		new HttpXmlClient().connect(port);
	}
}
