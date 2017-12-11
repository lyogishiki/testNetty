package netty.echo.object;


import javax.net.ssl.SSLException;

import netty.Consts;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

public class ObjectEchoServer {

	public static void main(String[] args) throws Exception {
		SelfSignedCertificate ssc = new SelfSignedCertificate();
		final SslContext sslCtx = SslContextBuilder.forServer(
				ssc.certificate(),ssc.privateKey()).build();
		
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
	
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workGroup)
				.channel(NioServerSocketChannel.class)
				.handler(new LoggingHandler())
				.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline pipeline = ch.pipeline();
						pipeline.addLast(sslCtx.newHandler(ch.alloc()));
						pipeline.addLast(new ObjectEncoder())
							.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)))
							.addLast(new ObjectEchoServerHandler());
					}
				});
			
			b.bind(Consts.HOST,Consts.PORT).sync().channel().closeFuture().sync();
		}finally {
			
		}
	}
}
