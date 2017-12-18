package netty2.chapter12;



import java.net.InetSocketAddress;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

public class SecureChatServer extends ChatServer{

	private final SslContext sslContext;

	public SecureChatServer(SslContext sslContext) {
		super();
		this.sslContext = sslContext;
	}

	@Override
	protected ChannelHandler createChannelInitializer() {
		return new SecureChatServerinitializer(channelGroup, sslContext);
	}
	
	public static void main(String[] args) throws CertificateException, SSLException {
		SelfSignedCertificate cert = new SelfSignedCertificate();
//		SSLContext sslContext = SSLContext.
		
		SslContext sslContext = SslContextBuilder
				.forServer(cert.certificate(), cert.privateKey())
				.build();
		
		
		SecureChatServer endPoint = new SecureChatServer(sslContext);
		
		ChannelFuture future = endPoint.start(
				new InetSocketAddress(9090));
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			endPoint.destory();
		} )) ;
		
		future.channel().closeFuture().syncUninterruptibly();
	}
}
