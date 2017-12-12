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
public class EchoServerHandler2 extends 
ChannelInboundHandlerAdapter{

	private ChannelHandlerContext ctx;
	
	public ChannelHandlerContext getCtx() {
		return ctx;
	}
	
	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		LOGGER.info("channelRegistered!!");
		super.channelRegistered(ctx);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		this.ctx = ctx;
		LOGGER.info("绑定ChannelHandlerContext!");
		super.channelActive(ctx);
	}



	private static final Logger LOGGER = LoggerFactory.getLogger(EchoServerHandler2.class);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf in = (ByteBuf) msg;
		LOGGER.warn(in.toString(StandardCharsets.UTF_8) + "-" + System.identityHashCode(ctx));
		ctx.fireChannelRead(msg);
	}

/*	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		LOGGER.info(cause.getMessage());
		cause.printStackTrace();
		ctx.close();	//关闭channel
	}*/
	
	

}
