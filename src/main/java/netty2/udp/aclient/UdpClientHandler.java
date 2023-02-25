package netty2.udp.aclient;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

public class UdpClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
		System.out.println("客戶端收到消息:" + msg.content().toString(StandardCharsets.UTF_8));

		ByteBuf byteBuf = Unpooled.copiedBuffer("你好服务器".getBytes(StandardCharsets.UTF_8));
		ctx.writeAndFlush(new DatagramPacket(byteBuf, msg.sender()))
		.addListener(ChannelFutureListener.CLOSE);
		
//		ctx.close();
	}

}
