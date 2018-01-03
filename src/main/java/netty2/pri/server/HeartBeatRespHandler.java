package netty2.pri.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandler.Sharable;
import netty2.pri.MessageType;
import netty2.pri.NettyMessage;
import netty2.pri.NettyMessage.Header;

@Sharable
public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter {

	public static final HeartBeatRespHandler DEFAULT_INSTANCE = new HeartBeatRespHandler();
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		NettyMessage<Object> message = (NettyMessage<Object>) msg;

		if (message.getType() == MessageType.HEARTBEAT_REQ) {
			System.out.println("Server Receive client heart beat message : " + message);
			NettyMessage<Void> heartBeat = buildHeatBeat();
			ctx.writeAndFlush(heartBeat);

		} else {
			ctx.fireChannelRead(msg);
		}
	}

	public final static NettyMessage<Void> PONG_MESSAGE = new NettyMessage<>(MessageType.HEARTBEAT_RESP);
	
	
	private NettyMessage<Void> buildHeatBeat() {
		return PONG_MESSAGE;
	}

}
