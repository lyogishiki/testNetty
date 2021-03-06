package netty2.echo.client;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import io.netty.util.back.ReferenceCountUtil;
import io.netty.util.internal.ThreadLocalRandom;

public class EchoClientHandler extends 
SimpleChannelInboundHandler<ByteBuf>{

	/*在客户端，当channelRead0()方法完成时，
	 * 你已经有了传入消息，并且已经处理完它了。当该方
	法返回时，SimpleChannelInboundHandler 
	负责释放指向保存该消息的ByteBuf 的内存引用，
	释放消息。ReferenceCountUtil.release(msg);
	
	所以channelRead0 这个方法不能够去调用ctx.write方法，因为异步执行的write
	可能会在ReferenceCountUtil.release(msg);之后调用，然后报错，
	
	在EchoServerHandler 中，
	你仍然需要将传入消息回送给发送者，
	而write()操作是异步的，直
	到channelRead()方法返回后可能仍然没有完成（如代码清单2-1 所示）。
	为此，EchoServerHandler
扩展了ChannelInboundHandlerAdapter，
	其在这个时间点上不会释放消息,
	而在channelReadComplete后释放资源
	
	*/
	private static final Logger LOGGER = LoggerFactory.getLogger(EchoClient.class);
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		
		LOGGER.warn(ctx.channel().attr(AttributeKey.valueOf("testKey")).toString());
		
		LOGGER.warn(
				"received message : "+msg.toString(StandardCharsets.UTF_8));
		LOGGER.info("EchoClientHandler.channelRead0()" + ctx.channel());
	}

	//当与服务器的连接建立后调用
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		
		for(int i=0;i<100;i++) {
			ctx.writeAndFlush(Unpooled.copiedBuffer(
					"Hello Netty!" + random.nextInt(10000), StandardCharsets.UTF_8));
		}
		
		
		//
		LOGGER.info("EchoClientHandler.channelActive()" + ctx.channel());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	
}
