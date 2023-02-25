package netty2.udp.aserve;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class UdpServer {

	public static void main(String[] args) {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group)	//
			.channel(NioDatagramChannel.class)
			.option(ChannelOption.SO_BROADCAST, true)
					.option(ChannelOption.SO_RCVBUF, 2048 * 1024)
					.handler(new ChannelInitializer<NioDatagramChannel>() {

						@Override
						protected void initChannel(NioDatagramChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast(new UdpServerHandler());
						}
					});

			ChannelFuture f = bootstrap.bind(8088).sync();
			System.out.println("服务器正在监听....");
			f.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
			group.shutdownGracefully();
		}

	}

}
