package netty.telnet;

import java.io.File;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLException;

import netty.Consts;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

public class TelnetServer {

	public static void main(String[] args) throws Exception {
		
		final SslContext sslCtx;
		if(Consts.SSL){
			SelfSignedCertificate ssc = new SelfSignedCertificate();
			sslCtx = SslContextBuilder
					.forServer(ssc.certificate(),ssc.privateKey()).build();
		}else{
			sslCtx = null;
		}
		
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		
		try{
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup,workGroup)
				.channel(NioServerSocketChannel.class)
				.handler(new LoggingHandler())
				.childHandler(new TelnetServerInitializer(sslCtx));
			
			b.bind(Consts.HOST, Consts.PORT).sync()
				.channel().closeFuture().sync();
		}finally{
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
		
	}
}
