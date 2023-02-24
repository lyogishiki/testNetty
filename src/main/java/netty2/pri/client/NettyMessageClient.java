package netty2.pri.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import netty.Consts;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

public class NettyMessageClient {

	private final InetSocketAddress remoteAddress;
	
	private final EventLoopGroup group;

	public NettyMessageClient(String host,int port) {
		this(new InetSocketAddress(host, port),new NioEventLoopGroup());
	}

	public NettyMessageClient(InetSocketAddress remoteAddress, EventLoopGroup group) {
		super();
		this.remoteAddress = remoteAddress;
		this.group = group;
	}

	public void connect() throws Exception {
		Bootstrap bootstrap = new Bootstrap()
				.channel(NioSocketChannel.class)
				.option(ChannelOption.SO_KEEPALIVE, true)
				.group(group)
				.handler(new NettyMessageClientInitializer());
		
		ChannelFuture channelFuture = bootstrap.connect(remoteAddress).sync();
		System.out.println("NettyMessageClient linked on :" + remoteAddress);
		channelFuture.channel().closeFuture().sync();
	}
	
	public void stop() {
		group.shutdownGracefully();
	}
	
	public static void main(String[] args) {
		NettyMessageClient client = new NettyMessageClient(
				new InetSocketAddress(Consts.HOST, 18081),	//	
				new NioEventLoopGroup(1));							//
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(;;) {
			try {
				client.connect();
			} catch (Exception e) {
				System.out.println("NettyMessageClient.main() 发生异常!");
				e.printStackTrace();
			}
			
			System.out.println(sdf.format(new Date()) + ":--------------重连中-----------");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e1) {
			}
		}
		
		
	}
	
}
