package netty.httpFile;

import java.security.cert.CertificateException;

import netty.Consts;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;
import io.netty.handler.ssl.util.SelfSignedCertificate;

public class HttpStaticFileServer {

	public static void main(String[] args) throws Exception {
		final SslContext sslCtx;
		if(Consts.SSL){
			SelfSignedCertificate ssc = new SelfSignedCertificate();
			sslCtx = SslContextBuilder.forServer(ssc.certificate(),ssc.privateKey())
					.sslProvider(SslProvider.JDK).build();
		}else{
			sslCtx = null;
		}
		
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.INFO))//
					.childHandler(new HttpStaticFileServerInitializer(sslCtx));
			Channel ch = b.bind(Consts.PORT).sync().channel();
			System.err.println("Open your web browser and navigate to " + (Consts.SSL ? "https" : "http")
					+ "://127.0.0.1:" + Consts.PORT + '/');
			ch.closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
}
