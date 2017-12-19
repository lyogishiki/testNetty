package netty2.chapter13.monitor;

import java.util.StringJoiner;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty2.chapter13.LogEvent;

public class LogEventHandler extends SimpleChannelInboundHandler<LogEvent>{

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, LogEvent msg) throws Exception {
		StringJoiner joiner = new StringJoiner(",", "[", "]");
		
		joiner.add(String.valueOf(msg.getReceived()))
			.add(msg.getSource().toString())
			.add(msg.getLogfile())
			.add(msg.getMsg());
		
		System.out.println(joiner.toString());
			
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	

}
