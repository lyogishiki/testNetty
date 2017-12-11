package netty.pri;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.concurrent.Future;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class NettyClient {

	private ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
	EventLoopGroup group = new NioEventLoopGroup();
	
	public void connect(String host,int port) throws InterruptedException{
		Bootstrap b = new Bootstrap();
		
		try {
			b.group(group).channel(NioSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new NettyMessageDecoder(1024*1024, 4, 4))
						.addLast("MessageEncoder",new NettyMessageEncoder())
						//超时handler
						.addLast("readTimeoutHandler",new ReadTimeoutHandler(50))
						.addLast("LoginAuthHandler",new LoginAuthReqHandler())
						.addLast("HeartBeathandler", new HeartBeatReqHandler());
					}
				});
			
			ChannelFuture future = b.connect(new InetSocketAddress(host, port),
					new InetSocketAddress("127.0.0.1", 12088))
					.sync();
			future.channel().closeFuture().sync();
		}finally {
			//所有资源释放完成后,清空资源，再次发起重练操作
			service.execute(new Runnable() {
				
				@Override
				public void run() {
					try {
						TimeUnit.SECONDS.sleep(NettyConstant.WAIT_TIME);
						connect(host, port);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
	
	public static void main(String[] args){
		try {
			new NettyClient().connect(NettyConstant.REMOTEIP, NettyConstant.PORT);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
