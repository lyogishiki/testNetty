package netty.echo;

import java.net.Socket;
import java.net.SocketOption;
import java.net.SocketOptions;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import javax.net.ssl.SSLException;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

public class EchoClient {

	static final boolean SSL = System.getProperty("ssl") != null;
	static final String HOST = System.getProperty("host","192.168.5.133");		
	static final int PORT = Integer.parseInt(System.getProperty("port","28085"));
	static final int SIZE = Integer.parseInt(System.getProperty("size","256"));
			
	
	public static void main(String[] args) throws Exception {
		final SslContext sslCtx;
		if(SSL){
			sslCtx = SslContextBuilder.forClient()
					.trustManager(InsecureTrustManagerFactory.INSTANCE)
					.build();
		}else{
			sslCtx = null;
		}
		
	
		
		EventLoopGroup group = new NioEventLoopGroup();
		try{
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline p = ch.pipeline();
						p.addLast(new LineBasedFrameDecoder(65536));
						p.addLast(new StringDecoder(StandardCharsets.UTF_8));
						if(sslCtx != null){
							p.addLast(sslCtx.newHandler(ch.alloc(),HOST,PORT));
						}
						
						p.addLast(new EchoClientHandler());
					}
				});
			
			ChannelFuture f = b.connect(HOST,PORT).sync();
			
			Channel channel = f.channel();
			System.out.println(1);
			Scanner sc = new Scanner(System.in);
			String str = "init";
			while(!"quit".equals(str)) {
				System.out.println("发送：" + str);
				str = sc.nextLine().trim();
				channel.writeAndFlush(Unpooled.wrappedBuffer((str+"\n").getBytes(StandardCharsets.UTF_8)));
			}
			sc.close();
			f.channel().closeFuture().sync();
		}finally{
			group.shutdownGracefully();
		}
	}
}
