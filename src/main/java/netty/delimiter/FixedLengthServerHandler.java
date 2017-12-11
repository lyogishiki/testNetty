package netty.delimiter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class FixedLengthServerHandler extends ChannelInboundHandlerAdapter {

	private AtomicInteger ai = new AtomicInteger(0);
	private ThreadLocal<SimpleDateFormat> sdfLocal = new ThreadLocal<SimpleDateFormat>(){

		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
		
	};
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String body = (String)msg;
		System.out.println(body + ",times : " + ai.incrementAndGet());
		SimpleDateFormat sdf = sdfLocal.get();
		String req = sdf.format(new Date());
		//System.out.println(sdf);
		req += "$_";
		ByteBuf echo = Unpooled.copiedBuffer(req.getBytes());
		ctx.write(echo);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}



}
