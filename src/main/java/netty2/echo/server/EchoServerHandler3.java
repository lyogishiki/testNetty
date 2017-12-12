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
public class EchoServerHandler3 extends 
ChannelInboundHandlerAdapter{

	private static final Logger LOGGER = LoggerFactory.getLogger(EchoServerHandler3.class);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf in = (ByteBuf) msg;
		LOGGER.info(in.toString(StandardCharsets.UTF_8) + "-" + System.identityHashCode(ctx));
	}


	/*@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();	//关闭channel
	}*/
	
	

}
