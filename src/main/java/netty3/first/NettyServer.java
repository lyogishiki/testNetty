package netty3.first;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import netty2.pri.server.NettyMessageServerInitializer;

/**
 * ChannelOption.RCVBUF_ALLOCATOR	接收缓冲区大小
 * ChannelOption.SO_BACKLOG			如果接收通道已经阻塞，可以有N个排队等待
 * @author ghost
 *
 */
public class NettyServer {

	public static void main(String[] args) {
		
		//默认线程数是cpu的两倍。
		EventLoopGroup bossGroup = new NioEventLoopGroup(2);
		EventLoopGroup workGroup = new NioEventLoopGroup(4);
		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup, workGroup)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 128)
			.option(ChannelOption.SO_KEEPALIVE, true)		//这个是设置childOption还是option
			.childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline()
					.addLast(new LineBasedFrameDecoder(1024))
					.addLast(new StringDecoder(StandardCharsets.UTF_8))
					.addLast(new SimpleHandler());
					
//					设置服务端处理使用的内存池
//					ch.config().setAllocator(PooledByteBufAllocator.DEFAULT)
				}
			});		
		
		
			ChannelFuture future = serverBootstrap.bind(8080).sync();
			
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
		
	}
	
}
