package jvm.hello;

import java.nio.charset.StandardCharsets;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class HelloServer {

	public static void main(String[] args) throws InterruptedException {
		ServerBootstrap bootstrap = new ServerBootstrap();
		EventLoopGroup group = new DefaultEventLoopGroup();
		
		// boss 处理连接，worker处理读写.
		bootstrap.group(new NioEventLoopGroup()).channel(NioServerSocketChannel.class)
				// Channel 和客户端数据读写的通道.
				// Initializer 初始化.负责添加别的handler.
				.childHandler(new ChannelInitializer<NioSocketChannel>() {

					@Override
					protected void initChannel(NioSocketChannel ch) throws Exception {
//						System.out.println("接受链接、");
						// ByteBuf 准换成字符串/
						ch.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8));
						ch.pipeline().addLast(new StringEncoder(StandardCharsets.UTF_8));
						ch.pipeline().addLast("handler_1",new ChannelInboundHandlerAdapter() {

							@Override
							public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
								String res =  System.currentTimeMillis() + ":" + msg;
//								如果想要把数据传给下一个channel handler.必须调用ctx.fireChannelRead(msg);
								ctx.fireChannelRead(res);
//								ctx.fireChannelRead(msg);
//								super.channelRead(ctx, res);
								System.out.println(Thread.currentThread());
							}

						}).addLast(group,"handler_2",new ChannelInboundHandlerAdapter() {

							@Override
							public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//								System.out.println(ctx.executor());
								System.out.println(msg);
								
//								我们指定这个handler使用defaultEventLoop后,不需要显示调用ctx.executor().execute();方法执行.
//								而是整个这个handler的方法都是由defaultEventLoop执行的.
								System.out.println(Thread.currentThread());
								ctx.writeAndFlush("huiqu...");
							}
							
						});

					}

				});
		
		ChannelFuture f = bootstrap.bind(8888).sync();
		
		System.out.println(1);
		f.channel().closeFuture()
		
		.sync();
	}

}
