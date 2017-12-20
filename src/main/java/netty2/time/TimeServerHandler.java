package netty2.time;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class TimeServerHandler extends ChannelInboundHandlerAdapter{
	
	ThreadLocal<SimpleDateFormat> localFormater = ThreadLocal.withInitial(() -> 
		new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		try {
			UpdTime time = (UpdTime) msg;
			System.out.println("receive :" + time);
			ctx.writeAndFlush(time);
		}finally {
			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	
	
}
