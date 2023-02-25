package netty2.udp.aserve;


import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

public class UdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket>{

	
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("UdpServerHandler.channelActive()");
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("UdpServerHandler.channelInactive()");
		super.channelInactive(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("UdpServerHandler.exceptionCaught()");
		super.exceptionCaught(ctx, cause);
	}
	
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		System.out.println("UdpServerHandler.handlerAdded()");
		super.handlerAdded(ctx);
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		System.out.println("UdpServerHandler.handlerRemoved()");
		super.handlerRemoved(ctx);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
		
		System.out.println("服务端接收到消息:" + msg.content().toString(StandardCharsets.UTF_8));
		
		ByteBuf byteBuf = Unpooled.copiedBuffer("服务端已经接收到消息!".getBytes(StandardCharsets.UTF_8));
		ctx.writeAndFlush(new DatagramPacket(byteBuf, msg.sender()));
//		ctx.writeAndFlush(byteBuf);
	}

}
