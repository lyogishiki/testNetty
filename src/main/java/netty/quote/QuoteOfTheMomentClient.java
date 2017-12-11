package netty.quote;

import java.net.InetSocketAddress;

import netty.Consts;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

public class QuoteOfTheMomentClient {
	
	public static void main(String[] args) throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		
		try{
			Bootstrap b = new Bootstrap();
			b.group(group)
				.channel(NioDatagramChannel.class)
				.option(ChannelOption.SO_BROADCAST, true)
				.handler(new QuoteOfTheMomentClientHandler());
			
			Channel ch = b.bind(0).sync().channel();
			
			/*DatagramPacket dg = new DatagramPacket(
					Unpooled.copiedBuffer("QOTM?", CharsetUtil.UTF_8), 
					new InetSocketAddress("255.255.255.255", Consts.PORT));
			
	        ch.writeAndFlush(dg).sync();*/
			
			ch.writeAndFlush(new DatagramPacket(
                    Unpooled.copiedBuffer("QOTM?", CharsetUtil.UTF_8),
                    new InetSocketAddress("255.255.255.255", Consts.PORT))).sync();
			
			if(!ch.closeFuture().await(5000)){
				System.err.println("QOTM request timed out.");
			}
		}finally{
			group.shutdownGracefully();
		}
	}
}
