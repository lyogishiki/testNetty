package netty.echo;


import java.sql.Date;
import java.sql.Timestamp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoServerHandler extends ChannelInboundHandlerAdapter{

	
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		System.out.println("客户端关闭链接！,"+ctx.channel().isActive()
				+ "," + ctx.channel().isOpen() + new Timestamp(System.currentTimeMillis()));
	}
	
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		System.out.println("客户端登录！！");
		super.handlerAdded(ctx);
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		System.out.println("客户端关闭链接！2,"+ctx.channel().isActive()
				+ "," + ctx.channel().isOpen() );
		super.handlerRemoved(ctx);
	}



	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("接收:"+msg +"," + ctx.channel().isActive() + "," + ctx.channel().isOpen());
		ctx.write(msg);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("发生异常！！！"+"," + ctx.channel().isActive() + "," + ctx.channel().isOpen());
		cause.printStackTrace();
		ctx.close();
		System.out.println("关闭连接！！！"+"," + ctx.channel().isActive() + "," + ctx.channel().isOpen());
	}
	
	

}
