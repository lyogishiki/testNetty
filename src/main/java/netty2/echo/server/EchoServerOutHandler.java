package netty2.echo.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class EchoServerOutHandler extends ChannelOutboundHandlerAdapter{

	@Override
	public void read(ChannelHandlerContext ctx) throws Exception {
		System.out.println("EchoServerOutHandler.read()");
		super.read(ctx);
	}

	
	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		System.out.println("EchoServerOutHandler.write()");
		System.out.println(msg);
		System.out.println(promise);
		
		ctx.channel().write(msg)
			.addListener((ChannelFuture f) -> {
				if(!f.isSuccess()) {
					f.cause().printStackTrace();
					f.channel().close();
				}
			});
//		super.write(ctx, msg, promise);
		
		
		
//		c
//		ReferenceCountUtil.release(msg);
//		通知ChannelPromise消息已被处理，使channelFutureListener
//		接收到通知.
//		promise.setSuccess();
//		
	}
	

	
	
	@Override
	public void flush(ChannelHandlerContext ctx) throws Exception {
		System.out.println("EchoServerOutHandler.flush()");
		super.flush(ctx);
	}

	
	
}
