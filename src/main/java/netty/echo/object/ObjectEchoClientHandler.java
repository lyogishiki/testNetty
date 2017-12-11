package netty.echo.object;

import java.util.ArrayList;
import java.util.List;

import netty.Consts;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

public class ObjectEchoClientHandler extends ChannelInboundHandlerAdapter {

	private final List<Integer> firstMessage;
	
	
	
	public ObjectEchoClientHandler() {
		super();
		firstMessage = new ArrayList<Integer>();
		for(int i=0;i<Consts.SIZE;i++){
			firstMessage.add(Integer.valueOf(i));
		}
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(firstMessage);
	}
	

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println(msg.getClass());
		System.out.println(msg);
		ctx.write(msg);
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
