package netty.proxy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import netty.Consts;

public class HexDumpProxy {
	
	public static void main(String[] args) throws InterruptedException {
		  System.err.println("Proxying *:" + Consts.LOCAL_PORT + " to " + Consts.REMOTE_HOST + ':' + Consts.REMOTE_PORT + " ...");
		  
		  EventLoopGroup bossGroup = new NioEventLoopGroup();
		  EventLoopGroup workGroup = new NioEventLoopGroup();
		  
		  try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workGroup)//
					.channel(NioServerSocketChannel.class)//
					.handler(new LoggingHandler(LogLevel.INFO))//
					.childHandler(new HexDumpProxyInitializer(Consts.REMOTE_HOST, Consts.REMOTE_PORT))
					.childOption(ChannelOption.AUTO_READ, false);
			b.bind(Consts.LOCAL_PORT).sync().channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}

}
