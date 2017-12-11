package netty.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class DiscardClientHandler extends SimpleChannelInboundHandler<Object> {

	private ByteBuf content;
	private ChannelHandlerContext ctx;
	
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		this.ctx = ctx;
		
		content = ctx.alloc().directBuffer(DiscardClient.SIZE)
				.writeZero(DiscardClient.SIZE);
		
		generateTraffic();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		content.release();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		
	}
	
	private void generateTraffic(){
		ByteBuf tmp1 = content.retainedDuplicate();
		System.out.println(tmp1);
		ctx.writeAndFlush(content.retainedDuplicate())
			.addListener(trafficGenerator);
	}
	
	private final ChannelFutureListener trafficGenerator = new ChannelFutureListener() {
		
		@Override
		public void operationComplete(ChannelFuture future) throws Exception {
			if(future.isSuccess()){
				generateTraffic();
			}else{
				future.cause().printStackTrace();
				
				future.channel().close();
			}
		}
	};
}
