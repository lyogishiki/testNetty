package netty.quote;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

import java.util.Random;

public class QuoteOftheMomentServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

	private static Random ran = new Random();

	private static final String[] quotes = { "Where there is love there is life.",
			"First they ignore you, then they laugh at you, then they fight you, then you win.",
			"Be the change you want to see in the world.",
			"The weak can never forgive. Forgiveness is the attribute of the strong.", };

	private static String nextQuote(){
		int quoteId;
		synchronized (ran) {
			quoteId = ran.nextInt(quotes.length);
		}
		return quotes[quoteId];
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
		System.out.println(msg);
		String str = msg.content().toString(CharsetUtil.UTF_8);
		System.out.println(msg);
		if ("QOTM?".equals(str)) {
			ctx.write(new DatagramPacket(
					Unpooled.copiedBuffer("QOTM: " + nextQuote(), 
					CharsetUtil.UTF_8),msg.sender()));
		}
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
	    ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
	    cause.printStackTrace();
	    // We don't close the channel because we can keep serving requests.
	}

}
