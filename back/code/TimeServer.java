package netty.code;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class TimeServer {

	public void bind(int port) throws InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			// 配置NIO线程组
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class) // 对应ServerSocketChannel
					.option(ChannelOption.SO_BACKLOG, 1024)
					// 处理网络IO时间，如记录日志，对消息编码解码
					.childHandler(new ChildChannelHandler());
			// 绑定端口，等待同步成功
			ChannelFuture f = b.bind(port).sync();
			// 等待服务端监听端口关闭
			f.channel().closeFuture().sync();
		} finally {
			// 优雅退出，释放线程池资源
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ch.pipeline()
				.addLast("frameDecoder",new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2))
				.addLast("msgPack Decode",new MsgpackDecoder())
				.addLast("framePrepender",new LengthFieldPrepender(2))
				.addLast("msgPack Encode",new MsgpackEncoder())
				.addLast(new TimeServerHandler());
		}

	}

	public static void main(String[] args) throws InterruptedException {
		int port = 8080;
		new TimeServer().bind(port);
	}
}
