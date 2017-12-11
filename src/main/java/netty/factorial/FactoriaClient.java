package netty.factorial;

import javax.net.ssl.SSLException;

import netty.Consts;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

public class FactoriaClient {

	static final int COUNT = Integer.parseInt(System.getProperty("count", "1000"));
	
	public static void main(String[] args) throws Exception {
		final SslContext sslCtx = SslContextBuilder.forClient()
				.trustManager(InsecureTrustManagerFactory.INSTANCE).build();
		
		EventLoopGroup group = new NioEventLoopGroup();
		
		try {
			Bootstrap b = new Bootstrap();
			b.group(group)
				.channel(NioSocketChannel.class)
				.handler(new FactoriaClientInitializer(sslCtx));
			
			ChannelFuture f = b.connect(Consts.HOST, Consts.PORT).sync();
			
//			获取channelHandler
			FactorialClientHandler handler = f.channel().pipeline().get(FactorialClientHandler.class);
			System.err.format("Factoria of %,d is : %,d", COUNT,handler.getFactorial());
		} finally {
			group.shutdownGracefully();
		}
	}
}
