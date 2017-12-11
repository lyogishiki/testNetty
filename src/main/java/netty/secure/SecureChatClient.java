package netty.secure;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.net.ssl.SSLException;

import netty.Consts;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

public class SecureChatClient {

	public static void main(String[] args) throws Exception {
		final SslContext sslCtx = SslContextBuilder.forClient()
				.trustManager(InsecureTrustManagerFactory.INSTANCE).build();
		
		EventLoopGroup group = new NioEventLoopGroup();
		try{
			
		
		Bootstrap b = new Bootstrap();
		b.group(group)
			.channel(NioSocketChannel.class)
			.handler(new SecureChatClientInitializer(sslCtx));
		
		Channel ch = b.connect(Consts.HOST, Consts.PORT).channel();
		ChannelFuture lastWriteFuture = null;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		for(;;){
			String line = in.readLine();
			if(line == null){
				break;
			}
			lastWriteFuture = ch.writeAndFlush(line+"\r\n");
			if("bye".equalsIgnoreCase(line)){
				ch.closeFuture().sync();
				break;
			}
		}
		
		if(lastWriteFuture != null){
			lastWriteFuture.sync();
		}
		}finally{
			group.shutdownGracefully();
		}
	}
}
