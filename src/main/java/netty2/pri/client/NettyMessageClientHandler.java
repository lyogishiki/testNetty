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
public class NettyMessageClientHandler extends SimpleChannelInboundHandler<NettyMessage<Object>> {

	public static final NettyMessageClientHandler DEFAULT_INSTANCE = new NettyMessageClientHandler();
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		NettyMessage<Object> nettyMessage = new NettyMessage<>(MessageType.SERVICE_REQ);
		Header header = new Header();
		header.setPriority((byte) 1);
		header.setSessionID(ThreadLocalRandom.current().nextLong());
		header.setLength(155);
		Map<String, Object> attachment = new HashMap<>();
		attachment.put("3463456456", "AA56456456");
		attachment.put("B4564564564B", "BB456456456456");
		header.setAttachment(attachment);
		nettyMessage.setHeader(header);

		ctx.writeAndFlush(nettyMessage);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, NettyMessage<Object> msg) throws Exception {
		
		System.out.println(ctx.alloc().getClass());
		System.out.println("NettyMessageClientHandler.channelRead0()" + msg);
		
		NettyMessage<Object> nettyMessage = new NettyMessage<>(MessageType.SERVICE_REQ);
		Header header = new Header();
		header.setPriority((byte) 1);
		header.setSessionID(ThreadLocalRandom.current().nextLong());
		header.setLength(155);
		Map<String, Object> attachment = new HashMap<>();
		attachment.put("AA", "AA");
		attachment.put("BB", "BB");
		header.setAttachment(attachment);
		nettyMessage.setHeader(header);

		ctx.write(nettyMessage);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		System.out.println("NettyMessageClientHandler.handlerAdded()");
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		System.out.println("NettyMessageClientHandler.handlerRemoved()");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		System.err.println("-------------------关闭连接-----------");
		ctx.close();
	}


	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			System.out.println(event + ":" + event.state() + "-------------------关闭连接-------------------------");
			System.out.println("发送心跳服务。");
			ctx.writeAndFlush(HeartBeatReqHandler.PING_MESSAGE);
//			ctx.close();
		} else {
			ctx.fireUserEventTriggered(evt);
		}
	}
}
