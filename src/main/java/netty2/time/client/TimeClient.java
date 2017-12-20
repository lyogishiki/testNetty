package netty2.time.client;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import netty2.time.UpdTimeDecoder;
import netty2.time.UpdTimeEncoder;

public class TimeClient {
	
	
	public void start(String hostname,int port) {
		NioEventLoopGroup group = new NioEventLoopGroup();
		
		try {
			Bootstrap bootstrap = new Bootstrap();
			
			bootstrap.group(group)
				.channel(NioSocketChannel.class)
				.remoteAddress(new InetSocketAddress(hostname, port))
				.handler(new ChannelInitializer<Channel>() {

					@Override
					protected void initChannel(Channel ch) throws Exception {
						ch.pipeline()
							.addLast(new UpdTimeEncoder())
							.addLast(new UpdTimeDecoder())
							.addLast(new TimeClientHandler());
					}
				});
			
			ChannelFuture future = bootstrap.connect().sync();
			future.channel().closeFuture().sync();
			
		} catch(Exception e){
			e.printStackTrace();
		}finally {
			group.shutdownGracefully().syncUninterruptibly();
		}
		
		
	}

	public static void main(String[] args) {
		TimeClient client = new TimeClient();
		client.start("127.0.0.1", 8080);
	}
	
}
