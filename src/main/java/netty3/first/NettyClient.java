package netty3.first;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.AttributeKey;

public class NettyClient {

	public static void main(String[] args) {
		EventLoopGroup group = new NioEventLoopGroup();
		
		try {
			Bootstrap bootstrap = new Bootstrap();
			
//			客户端设置内存池设置
//			bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
			
			bootstrap.channel(NioSocketChannel.class)
				.group(group)
				.option(ChannelOption.SO_KEEPALIVE, true)
				.handler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						
						ch.attr(AttributeKey.valueOf("key")).set("value:tmp1234");
						ch.pipeline()
//						.addLast(new delimiter)
						.addLast(new LineBasedFrameDecoder(1024))
						.addLast(new StringDecoder(StandardCharsets.UTF_8))
						.addLast(new ClientHandler());
					}
				});
			
			ChannelFuture future = bootstrap.connect(new InetSocketAddress("127.0.0.1", 8080)).sync();
			
			
			future.channel().writeAndFlush(Unpooled.copiedBuffer("hello Netty!\r\n",StandardCharsets.UTF_8)).sync();
			
			future.channel().closeFuture().sync();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}
	
}
