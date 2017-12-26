package netty2.pri.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty2.pri.MessageType;
import netty2.pri.NettyMessage;
import netty2.pri.NettyMessage.Header;

public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		NettyMessage<Object> message = (NettyMessage<Object>) msg;

		if (message.getHeader() != null && message.getHeader().getType() == MessageType.HEARTBEAT_REQ) {
			System.out.println("Server Receive client heart beat message : " + message);
			NettyMessage<Void> heartBeat = buildHeatBeat();
			ctx.writeAndFlush(heartBeat);

		} else {
			ctx.fireChannelRead(msg);
		}
	}

	private final static NettyMessage<Void> PONG_MESSAGE;
	static {
		PONG_MESSAGE = new NettyMessage<>();
		Header header = new Header();
		header.setType(MessageType.HEARTBEAT_RESP);
		PONG_MESSAGE.setHeader(header);
	}
	
	private NettyMessage<Void> buildHeatBeat() {
		return PONG_MESSAGE;
	}

}
