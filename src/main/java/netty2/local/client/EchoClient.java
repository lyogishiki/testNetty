package netty2.local.client;

import java.net.InetSocketAddress;

import org.springframework.boot.autoconfigure.web.ErrorViewResolver;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.local.LocalAddress;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class EchoClient {

	public void start() throws Exception{
		
		EventLoopGroup group = 
				new DefaultEventLoopGroup(1);
		
		LocalAddress address = new LocalAddress("127.0.0.1");
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(LocalChannel.class)
			.handler(
					new ChannelInitializer<LocalChannel>() {
				@Override
				protected void initChannel(LocalChannel ch) throws Exception {
					System.out.println("channelInitializer"+ch);
					ch.pipeline()
					.addLast(new EchoClientHandler());
				}
			});
			

			
			ChannelFuture future = b.connect(address).sync();
			
//			System.out.println(f.channel().getClass());
//			System.out.println("mmmm"+f.channel());
//			System.out.println(f.channel().alloc());
			future.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully().sync();
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		String host = "127.0.0.1";
		int port = 9090;
		/*for(int i=0;i<10;i++) {
			new Thread(() -> {
				try {
				} catch (Exception e) {
					e.printStackTrace();
				}
			}).start();
			
		}*/
		new EchoClient().start();
		long end = System.currentTimeMillis();
		
		System.out.println(end - start);
	}	
}
