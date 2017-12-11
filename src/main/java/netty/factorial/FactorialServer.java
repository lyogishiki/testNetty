package netty.factorial;

import java.security.cert.CertificateException;

import sun.launcher.resources.launcher;
import netty.Consts;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

public class FactorialServer {
 
	public static void main(String[] args) throws Exception {
		SelfSignedCertificate ssc = new SelfSignedCertificate();
		final SslContext sslCtx = SslContextBuilder.forServer(
				ssc.certificate(),ssc.privateKey()).build();
		
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workGroup).channel(NioServerSocketChannel.class).handler(new LoggingHandler())
					.childHandler(new FactorialServerInitializer(sslCtx));
			b.bind(Consts.PORT).sync().channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
}
