package netty2.echo.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.ResourceLeakDetector.Level;

public class EchoServer {
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EchoServer.class);
	
	private final int port;
	public EchoServer(int port) {
		super();
		this.port = port;
	}
	
	public static void main(String[] args) throws InterruptedException {
		
		ResourceLeakDetector.setLevel(Level.PARANOID);
		
		int port = 9090;
		
		new EchoServer(port).start();
	}
	
	public void start() throws InterruptedException {
		
		ResourceLeakDetector.setLevel(Level.SIMPLE);
		
		final EchoServerHandler serverHandler = 
				new EchoServerHandler();
		
		//接收新链接
		EventLoopGroup bossGroup = new NioEventLoopGroup(2);
		//处理新链接
		EventLoopGroup workGroup = new NioEventLoopGroup(6);
		
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workGroup)	//
			.channel(NioServerSocketChannel.class)
//			.option(option, value)			//NioServerSocket 接受连接的使用
//			.childOption(childOption, value)	//接收到连接后，新建的和客户端通信的Channel的Option
			.localAddress(port)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							//					无状态的ChannelHandler可以被标注为@Shareable，被标注为@Shareable的
							//					ChannelHandler总是可以使用同样的实例。
							ch.pipeline()
							.addLast(serverHandler);
							//.addLast(new EchoServerHandler2());
//							.addLast(new EchoServerHandler3());
//							.addLast(new EchoServerOutHandler());
						}
					});
			//sync 阻塞等待知道绑定完成
			ChannelFuture future = bootstrap.bind().sync();
			
			System.out.println(future.channel().getClass());
			//获取closeFuture 并等待完成
			future.channel().closeFuture().sync();	//
			
		} finally {
			bossGroup.shutdownGracefully().sync();
			workGroup.shutdownGracefully().sync();
		}
	}
}
