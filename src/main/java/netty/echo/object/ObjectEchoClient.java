package netty.echo.object;

import javax.net.ssl.SSLException;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import netty.Consts;

public class ObjectEchoClient {

	public static void main(String[] args) throws Exception {
		final SslContext sslCtx = SslContextBuilder.forClient()
				.trustManager(InsecureTrustManagerFactory.INSTANCE)
				.build();
		
		EventLoopGroup group = new NioEventLoopGroup();
		try{
			Bootstrap b = new Bootstrap();
			b.group(group)
				.channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline pipeline = ch.pipeline();
						
						pipeline.addLast(sslCtx.newHandler(ch.alloc(),Consts.HOST,Consts.PORT))
							.addLast(new ObjectEncoder())
							.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)))
							.addLast(new ObjectEchoClientHandler());
					}
				});
			
			b.connect(Consts.HOST,Consts.PORT).sync().channel().closeFuture().sync();
		}finally{
			group.shutdownGracefully();
		}
	}
}
