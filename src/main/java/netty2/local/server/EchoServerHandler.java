package netty2.local.server;

import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

@Sharable		//标识可以被安全的共享
public class EchoServerHandler extends 
ChannelInboundHandlerAdapter{

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
	
		System.out.println(msg.getClass());
		
		
//		ByteBufUtil.
//		ctx.alloc()
		
		ByteBuf in = (ByteBuf) msg;
		String receiveMsg = in.toString(StandardCharsets.UTF_8);
		
		System.out.println(Thread.currentThread().getId() + ":"
				+ Thread.currentThread().getName() + "---"
				+ "server receive : " + receiveMsg);
		System.out.println(ctx.channel().getClass());
		
		
		
		ctx.write(in);
		/*new Thread(() -> {
			try {
				Thread.sleep(5000);
				ctx.writeAndFlush(in).addListener(ChannelFutureListener.CLOSE);	//只发送，不flush
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();*/
	}
	
	

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
//		System.out.println("EchoServerHandler.channelRegistered()");
		super.channelRegistered(ctx);
	}



	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
		//发送完成之后关闭该Channel
			.addListener(ChannelFutureListener.CLOSE);
		
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();	//关闭channel
	}
	
	

}
