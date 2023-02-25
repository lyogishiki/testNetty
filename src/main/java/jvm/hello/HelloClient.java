package jvm.hello;

import java.nio.charset.StandardCharsets;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class HelloClient {

	public static void main(String[] args) throws InterruptedException {
		Bootstrap bootstrap = new Bootstrap();
		NioEventLoopGroup boss = new NioEventLoopGroup(1);
		try {
			bootstrap.group(boss).channel(NioSocketChannel.class).handler(new ChannelInitializer<NioSocketChannel>() {
				// 在连接建立后调用initChannel
				@Override
				protected void initChannel(NioSocketChannel ch) throws Exception {
					ch.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8));
					ch.pipeline().addLast(new StringEncoder(StandardCharsets.UTF_8));
					ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {

						@Override
						public void channelActive(ChannelHandlerContext ctx) throws Exception {
							ctx.writeAndFlush("222223333")
//							添加一个发送成功的Listener,在发送成功后,关闭.
							.addListener(ChannelFutureListener.CLOSE);
							
						}

						@Override
						protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
							System.out.println(msg);
							ctx.fireChannelRead(msg);
//							super.channelRead(ctx, msg);
						}

					});
				}
			});
			// 阻塞方法,等待连接建立.
			ChannelFuture future = bootstrap.connect("127.0.0.1", 8888).sync();
			
			future.channel()
					// 这个write会在ChannelInboundHandler的channelActive后执行.
					.writeAndFlush("看看先后...");
			
			future.channel().closeFuture().sync();
			
//			future.channel().close().addListener(f -> {
//
//				System.out.println("关闭连接了!" + f + "," + f.getNow() + "," + f.isSuccess());
//			}).sync();

			// ChannelFuture future = bootstrap.connect("127.0.0.1", 8888);
			//
			// future.addListener(f -> {
			// future.channel().writeAndFlush("能发.看看先后1111...");
			// });
			// try {
			// Thread.sleep(5000);
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			// System.out.println(2);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			boss.shutdownGracefully().sync();
		}

	}

}
