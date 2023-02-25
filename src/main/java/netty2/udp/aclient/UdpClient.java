package netty2.udp.aclient;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class UdpClient {

	public static void main(String[] args) {
		EventLoopGroup group = new NioEventLoopGroup();

		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group).channel(NioDatagramChannel.class)
					.handler(new ChannelInitializer<NioDatagramChannel>() {

						@Override
						protected void initChannel(NioDatagramChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast(new UdpClientHandler());
						}
					});
			Channel channel = bootstrap.bind(8089).sync().channel();
			InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8088);
			ByteBuf byteBuf = Unpooled.copiedBuffer("你好".getBytes(StandardCharsets.UTF_8));
			channel.writeAndFlush(new DatagramPacket(byteBuf, address)).sync();
//			channel.close().await();
			
			channel.closeFuture().await();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}
}
