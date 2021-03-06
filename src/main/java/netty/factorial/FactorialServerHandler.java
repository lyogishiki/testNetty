package netty.factorial;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.math.BigInteger;

public class FactorialServerHandler extends SimpleChannelInboundHandler<BigInteger> {

	private BigInteger lastMultiplier = new BigInteger("1");
	private BigInteger factorial = new BigInteger("0");

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, BigInteger msg) throws Exception {
		//System.out.println(msg);
		lastMultiplier = msg;
		factorial = factorial.add(msg);
		ctx.writeAndFlush(factorial);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.err.printf("Factorial of %,d is: %,d%n", lastMultiplier, factorial);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}
