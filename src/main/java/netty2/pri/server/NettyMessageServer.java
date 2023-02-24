package netty2.pri.server;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ReferenceCountUtil;
import netty.Consts;
import netty2.base.protostuff.SerializeUtils;
import netty2.pri.MessageType;
import netty2.pri.NettyMessage;
import netty2.pri.ServerContext;
import netty2.pri.NettyMessage.Header;

public class NettyMessageServer {

	private final String host;
	private final int port;
	
	private final EventLoopGroup bossGroup;
	private final EventLoopGroup workGroup;
	
	public NettyMessageServer(String host, int port) {
		this(host, port, new NioEventLoopGroup(2), new NioEventLoopGroup());
	}

	public NettyMessageServer(String host, int port, EventLoopGroup bossGroup, EventLoopGroup workGroup) {
		super();
		this.host = host;
		this.port = port;
		this.bossGroup = bossGroup;
		this.workGroup = workGroup;
	}
	
	public void start() throws Exception {
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		
		serverBootstrap.
			group(bossGroup, workGroup)
			.channel(NioServerSocketChannel.class)
//			.option(ChannelOption.SO_BACKLOG, 512)
//			.option(ChannelOption.SO_RCVBUF, 128 * 1024)
			.childOption(ChannelOption.SO_KEEPALIVE, true)
			.childOption(ChannelOption.SO_SNDBUF, 128 * 1024)
			.childOption(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(128,2048,655360))
			.localAddress(new InetSocketAddress(host, port))
			.childHandler(new NettyMessageServerInitializer());		
		
		ChannelFuture future = serverBootstrap.bind().sync();
		
		System.out.println("Channel Server startup on " + host + ":" + port);
		//阻塞等待有关闭实践，也就是调用了close方法。
		future.channel().closeFuture().sync();
	}
	
	public void stop() {
		bossGroup.shutdownGracefully();
		workGroup.shutdownGracefully();
	}
	
	public static void main(String[] args) {
		NettyMessageServer server = new NettyMessageServer(Consts.HOST	, Consts.PORT);
		
		ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
		try {
			
			//启动推送消息，当然业务场景，需要根据自己的业务需要，启动监听消息队列啊，之类的服务。
			service.scheduleWithFixedDelay(() -> {
				
				Collection<Channel> channels = ServerContext.allLoginChannel();
				
				channels.stream().forEach(c -> {
					NettyMessage<Object> nettyMessage = new NettyMessage<>(MessageType.SERVICE_RESP);
					Header header = new Header();
					header.setPriority((byte) 1);
					header.setSessionID(ThreadLocalRandom.current().nextLong());
					header.setLength(155);
					Map<String, Object> attachment = new HashMap<>();
					attachment.put("CC", "FFFF");
					attachment.put("CDDD", "FFFEEEE");
					header.setAttachment(attachment);
					nettyMessage.setHeader(header);
					long current = System.currentTimeMillis();
					nettyMessage.setBody(current);
//					业务线程调用writeAndFlush 并不会阻塞我的业务线程，调用writeAndFlush 发送的数据还是会到NIO线程去执行
//					所以我的业务线程如果要申请ByteBuf的话可能要自己释放，如果没有申请ByteBuf，那么就由Netty框架释放，而且确实释放了，
//					因为Encoder申请的和释放的  都是在同一个NIO线程中的,所以啊 业务线程尽量不要创建Bytebuf 然后传递给 io线程。
//					ByteBuf buf = c.alloc().buffer(1024);
//					buf.writeBytes(SerializeUtils.serializer(nettyMessage));
					c.writeAndFlush(nettyMessage);
					//ReferenceCountUtil.release(buf);
				});
				
			}, 130, 130, TimeUnit.SECONDS);
			
			
			
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			server.stop();
		}
	}
	
}
