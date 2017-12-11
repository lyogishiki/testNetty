package netty.pri;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class HeartBeatReqHandler extends ChannelInboundHandlerAdapter {

	private volatile ScheduledFuture<?> heartBeat;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		NettyMessage message = (NettyMessage) msg;
		//握手成功，主动发送心跳消息
		if(message.getHeader() != null &&
				message.getHeader().getType() == MessageType.LOGIN_RESP.value()){
			//如果握手成功，启动定时器发送消息
			heartBeat = ctx.executor().scheduleWithFixedDelay(new HeartBeatTask(ctx),
					0, 5000, TimeUnit.MILLISECONDS);
		//接收服务端的心跳消息
		}else if(message.getHeader() != null && 
				message.getHeader().getType() == MessageType.HEARTBEAT_RESP.value()){
			System.out.println("Client receive server heart beat message : " + message);
		}else{
			ctx.fireChannelRead(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		if(heartBeat != null){
			heartBeat.cancel(true);
			heartBeat = null;
		}
		ctx.fireExceptionCaught(cause);
	}
	
	private class HeartBeatTask implements Runnable{
		private ChannelHandlerContext ctx;

		public HeartBeatTask(ChannelHandlerContext ctx) {
			super();
			this.ctx = ctx;
		}

		@Override
		public void run() {
			NettyMessage heatBeat = buildHeatBeat();
			System.out.println("Client send heart beat message to Server ; "
					+ heatBeat);
			ctx.writeAndFlush(heatBeat);
		}
		
		private NettyMessage buildHeatBeat(){
			NettyMessage message = new NettyMessage();
			Header header = new Header();
			header.setType(MessageType.HEARTBEAT_REQ.value());
			message.setHeader(header);
			return message;
		}
	}

}
