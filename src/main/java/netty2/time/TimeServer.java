package netty2.time;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TimeServer {

	public void start(String hostname,int port) {
		
		NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		NioEventLoopGroup workGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap bootstrap = new ServerBootstrap()
					.group(bossGroup, workGroup)
					.channel(NioServerSocketChannel.class)
					.localAddress(new InetSocketAddress(hostname, port))
					.handler(new ChannelInboundHandlerAdapter() {

						@Override
						public void channelActive(ChannelHandlerContext ctx) throws Exception {
							System.out.println(
									"TimeServer.start(...).new ChannelInboundHandlerAdapter() {...}.channelActive()");
							ctx.fireChannelActive();
						}

						@Override
						public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
							System.out.println(
									"TimeServer.start(...).new ChannelInboundHandlerAdapter() {...}.channelRead()");
							ctx.fireChannelRead(msg);
						}
						
					})
					.childHandler(new ChannelInitializer<Channel>() {

						@Override
						protected void initChannel(Channel ch) throws Exception {
							ch.pipeline()
							.addLast(new UpdTimeEncoder())
							.addLast(new UpdTimeDecoder())
							.addLast(new TimeServerHandler());
						}
					});
			
			ChannelFuture future = bootstrap.bind().sync();
			future.channel().closeFuture().sync();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			
			bossGroup.shutdownGracefully().syncUninterruptibly();
			workGroup.shutdownGracefully().syncUninterruptibly();
		}
		
	}
	
	public static void main(String[] args) {
		TimeServer timeServer = new TimeServer();
		timeServer.start("127.0.0.1", 8080);
	}
	
}
