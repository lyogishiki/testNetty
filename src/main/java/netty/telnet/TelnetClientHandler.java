package netty.telnet;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

public class TelnetClientHandler extends SimpleChannelInboundHandler<String> {

	/*@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		ChannelFuture future = null;
		for(;;){
			String line = br.readLine();
			if(line == null){
				break;
			}
			future = ctx.writeAndFlush(line + "\r\n");
			
			if("bye".equalsIgnoreCase(line)){
				ctx.close().sync();
				break;
			}
		}
		
		if(future != null){
			future.sync();
		}
	}*/

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		System.out.println(msg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}
