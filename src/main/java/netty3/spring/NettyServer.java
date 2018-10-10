package netty3.spring;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

@Component
public class NettyServer implements ApplicationListener<ContextRefreshedEvent>, Ordered {

	@Override
	public int getOrder() {
		return 11;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		NioEventLoopGroup bossGroup = new NioEventLoopGroup(2);
		NioEventLoopGroup workGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							// ch.pipeline().addLast(handlers)
							System.out.println(
									"NettyServer.onApplicationEvent(...).new ChannelInitializer() {...}.initChannel()");
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
