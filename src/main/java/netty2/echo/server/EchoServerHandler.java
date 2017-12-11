package netty2.echo.server;

import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.ThreadLocalRandom;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

@Sharable		//标识可以被安全的共享
public class EchoServerHandler extends 
ChannelInboundHandlerAdapter{

	private static final Logger LOGGER = LoggerFactory.getLogger(EchoServerHandler.class);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		//System.out.println(msg.getClass());
		
		ByteBuf in = (ByteBuf) msg;
		String receiveMsg = in.toString(StandardCharsets.UTF_8);
		ThreadLocalRandom random = ThreadLocalRandom.current();
		System.out.println(Thread.currentThread().getId() + ":"
				+ Thread.currentThread().getName() + "---"
				+ "server receive : " + receiveMsg);
		//System.out.println(ctx.channel().getClass());
		
		ByteBuf buf = ctx.alloc().heapBuffer(1024);
		buf.writeBytes(Unpooled.copiedBuffer(
				"hello World!this is a message" +
						random.nextInt(10000),StandardCharsets.UTF_8));
		
//		ctx.write(in);
		ctx.writeAndFlush(buf)
		.addListener(ChannelFutureListener.CLOSE);
		
//		ReferenceCountUtil.release(msg);
		
		/*new Thread(() -> {
			try {
				Thread.sleep(5000);
				ctx.writeAndFlush(in).addListener(ChannelFutureListener.CLOSE);	//只发送，不flush
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();*/
		
//		ReferenceCountUtil.release(msg);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//		调用Channel写触发CHannelOutboundHandler中的write方法。
//		ctx.channel().writeAndFlush(Unpooled.EMPTY_BUFFER)
//		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
//		.addListener(ChannelFutureListener.CLOSE);
		
//		ctx.flush().close();
		//ctx.close();
		//发送完成之后关闭该Channel
		
		/*PooledByteBufAllocator allocator = (PooledByteBufAllocator) ctx.alloc();
		LOGGER.info(ctx.alloc().getClass().toString());
		LOGGER.info(f.channel().getClass().toString());*/
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();	//关闭channel
	}
	
	

}
