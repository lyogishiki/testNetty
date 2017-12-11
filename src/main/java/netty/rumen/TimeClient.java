package netty.rumen;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;

public class TimeClient {
	
	public void connect(int port,String host) throws InterruptedException{
		EventLoopGroup group = new NioEventLoopGroup();
		
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group).channel(NioSocketChannel.class)
					.option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							//按行切割数据
							ch.pipeline().addLast(new LineBasedFrameDecoder(1024))
							//把bytebuf转为String类型
									.addLast(new StringDecoder())
									.addLast(new TimeClientHandler());
						}
					});
			ChannelFuture f = bootstrap.connect(host,port).sync();
			f.channel()
				.closeFuture().sync();
		}finally {
			group.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		
		TimeClient tc = new TimeClient();
		tc.connect(8080, "127.0.0.1");
	}
}
