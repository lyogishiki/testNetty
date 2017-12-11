package netty.worldclock;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.util.Arrays;
import java.util.List;

import javax.net.ssl.SSLException;

import netty.Consts;
import sun.security.acl.WorldGroupImpl;

public class WorkClockClient {

	static final List<String> CITIES = Arrays.asList(System.getProperty(
            "cities", "Asia/Seoul,Europe/Berlin,America/Los_Angeles").split(","));
	
	public static void main(String[] args) throws Exception {
		
		final SslContext sslCtx = SslContextBuilder.forClient()
					.trustManager(InsecureTrustManagerFactory.INSTANCE)
					.build();
		
		EventLoopGroup group = new NioEventLoopGroup();
		
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).handler(new WorldClockClientInitializer(sslCtx));
			Channel ch = b.connect(Consts.HOST, Consts.PORT).sync().channel();
			WorldClockClientHandler handler = ch.pipeline().get(WorldClockClientHandler.class);
			List<String> response = handler.getLocalTimes(CITIES);
			ch.close();
			for (int i = 0; i < CITIES.size(); i++) {
				System.out.format("%28s: %s%n", CITIES.get(i), response.get(i));
			}
		} finally {
			group.shutdownGracefully();
		}
	}
}
