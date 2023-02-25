package jvm.hello;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.embedded.EmbeddedChannel;

// EmbededChannel 可以快速用来测试channelHandler
public class TestEmbededChannel {

	public static void main(String[] args) {
		ChannelInboundHandler h1 = new ChannelInboundHandlerAdapter() {

			@Override
			public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
				System.out.println(1 + "," + msg);
				ByteBuf buf = (ByteBuf) msg;
				super.channelRead(ctx, new String(buf.array(), StandardCharsets.UTF_8));
			}

		};

		ChannelInboundHandler h2 = new ChannelInboundHandlerAdapter() {

			@Override
			public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
				System.out.println(2 + "," + msg);
				super.channelRead(ctx, msg);
				ctx.writeAndFlush("hhh");
				// 使用channel的writeAndFlush方法会调用 后面的后面的ChannelHandler.
				// 使用ctx的writeAndFLush方法,智慧调用这个handler方法之前的channelHandler.
				// ctx.channel().writeAndFlush("hhh");
			}

		};

		ChannelOutboundHandler h3 = new ChannelOutboundHandlerAdapter() {

			@Override
			public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
				System.out.println(3 + "," + msg);
				super.write(ctx, "我变了", promise);
			}

		};

		ChannelOutboundHandler h4 = new ChannelOutboundHandlerAdapter() {

			@Override
			public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
				System.out.println(4 + "," + msg);
				super.write(ctx, msg, promise);
			}

		};

		EmbeddedChannel channel = new EmbeddedChannel(h4, h3, h1, h2);
		channel.writeInbound(Unpooled.wrappedBuffer("你好啊".getBytes(StandardCharsets.UTF_8)));
	}

}
