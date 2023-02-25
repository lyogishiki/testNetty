package jvm.hello;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class HelloClient2 {

	public static void main(String[] args) throws InterruptedException {
		NioEventLoopGroup boss = new NioEventLoopGroup(4);
		try {
			CountDownLatch latch = new CountDownLatch(10000);
			ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
			
			for (int i = 0; i < 10000; i++) {
				Bootstrap bootstrap = new Bootstrap();
				bootstrap.group(boss).channel(NioSocketChannel.class)
						.handler(new ChannelInitializer<NioSocketChannel>() {
							// 在连接建立后调用initChannel
							@Override
							protected void initChannel(NioSocketChannel ch) throws Exception {
								ch.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8));
								ch.pipeline().addLast(new StringEncoder(StandardCharsets.UTF_8));
								ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {

									@Override
									public void channelActive(ChannelHandlerContext ctx) throws Exception {
										ctx.writeAndFlush(System.currentTimeMillis() + ":222223333");
									}

									@Override
									protected void channelRead0(ChannelHandlerContext ctx, String msg)
											throws Exception {
										String r = System.currentTimeMillis() + ":" + msg;
										queue.offer(r);
										ctx.close();
										latch.countDown();
									}
									

								});
							}
						});
				bootstrap.connect("127.0.0.1", 8888);
				
			}
			
			latch.await();
			
			for(String s : queue) {
				System.out.println(s);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			boss.shutdownGracefully().sync();
		}

	}

}
