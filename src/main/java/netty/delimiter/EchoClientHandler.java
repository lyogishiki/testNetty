package netty.delimiter;

import java.util.concurrent.atomic.AtomicInteger;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {

	private static String ECHO_STR = "Hi Netty ! Query time.$_";
//	private static String ECHO_STR = "Hi Netty !";
	private AtomicInteger ai = new AtomicInteger(0);
	
	/*@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		for(int i=0;i<100;i++){
			ctx.writeAndFlush(Unpooled.copiedBuffer(ECHO_STR.getBytes()));
		}
	}*/
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		for(int i=0;i<100;i++){
			ctx.writeAndFlush(Unpooled.copiedBuffer(ECHO_STR.getBytes()));
		}
		//
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println(msg + ",times : " + ai.incrementAndGet());
		//ctx.close().addListener(ChannelFutureListener.CLOSE).sync();
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
		ctx.close().addListener(ChannelFutureListener.CLOSE).sync();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	
}
