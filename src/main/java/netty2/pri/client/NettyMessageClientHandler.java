package netty2.pri.client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.timeout.IdleStateEvent;
import netty2.pri.MessageType;
import netty2.pri.NettyMessage;
import netty2.pri.NettyMessage.Header;

@Sharable
public class NettyMessageClientHandler extends SimpleChannelInboundHandler<NettyMessage<Object>>{

	public static final NettyMessageClientHandler DEFAULT_INSTANCE = new NettyMessageClientHandler();
	
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, NettyMessage<Object> msg) throws Exception {
		System.out.println("NettyMessageClientHandler.channelRead0()");
		for(int i=0;i<100;i++) {
			NettyMessage<Object> nettyMessage = new NettyMessage<>();
			Header header = new Header();
			header.setPriority((byte)1);
			header.setSessionID(ThreadLocalRandom.current().nextLong());
			header.setType(MessageType.SERVICE_REQ);
			header.setLength(155);
			Map<String, Object> attachment = new HashMap<>();
			attachment.put("AA", "AA");
			attachment.put("BB", "BB");
			header.setAttachment(attachment);
			nettyMessage.setHeader(header);
			
			ctx.write(nettyMessage);
		}
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

	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if(evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			System.out.println(event + ":" + event.state() + "-------------------关闭连接-------------------------");
			ctx.close();
		}else {
			ctx.fireUserEventTriggered(evt);
		}
	}
}
