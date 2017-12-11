package netty.snoop;

import javax.net.ssl.SSLException;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import netty.Consts;

public class HttpSnoopServer {

	static final boolean SSL = System.getProperty("ssl") != null;

	public static void main(String[] args) throws Exception {
		final SslContext sslCtx;
		if(SSL){
			SelfSignedCertificate ssc = new SelfSignedCertificate();
			sslCtx = SslContextBuilder.forServer(ssc.certificate(),
					ssc.privateKey()).build();
		}else{
			sslCtx = null;
		}
		
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workGroup).channel(NioServerSocketChannel.class).handler(new LoggingHandler())
					.childHandler(new HttpSnoopServerInitializer(sslCtx));
			Channel ch = b.bind(Consts.PORT).sync().channel();
			System.err.println("Open your web browser and navigate to " + (SSL ? "https" : "http") + "://127.0.0.1:"
					+ Consts.PORT + '/');
			ch.closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
		
	}

}
