package netty2.pri.client;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.ScheduledFuture;
import netty2.pri.MessageType;
import netty2.pri.NettyMessage;

public class HeartBeatReqHandler extends ChannelInboundHandlerAdapter{
	
	private volatile ScheduledFuture<?> heartBeat;
	
	public final static NettyMessage<Void> PING_MESSAGE = new NettyMessage<>(MessageType.HEARTBEAT_REQ);
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		NettyMessage<Object> message = (NettyMessage<Object>) msg;
//		握手成功,主动发送心跳消息
		if(message.getType() == MessageType.LOGIN_RESP) {
//		如果握手成功，那么就启动定时器发送消息。
//MYTASK	关注这里的定时任务，如果没有传递DefaultEventExecutorGroup ,那么就是用的是 EventLoopGroup也就是IO
//		线程，客户端的话应该是没什么问题，使用默认的eventLoopGroup也是没有问题的
//			,如果是服务端，而且任务是长时间执行的话，应该需要 传递一个DefaultEventLoopGroup，
//			避免阻塞IO线程。长时间阻塞IO线程导致服务器端处理能力下降。
			EventExecutor executor = ctx.executor();
			heartBeat = executor.scheduleWithFixedDelay(	//
					new HeartBeatTask(ctx), 0, 50, TimeUnit.SECONDS);
			
			
			//登陆成功之后，既需要建立心跳，也要可能要发送一些消息，
			ctx.fireChannelRead(msg);
			
		} else if(message.getType() == MessageType.HEARTBEAT_RESP) {
			System.out.println("Client receive server heart beat message : " + message);
		} else {
			ctx.fireChannelRead(msg);
		}
		
	}

	//我处理完之后，如果说后续的Handler也需要一些处理，那么就传递给后面的去处理。
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		//cause.printStackTrace();
		shutdown(ctx.executor());
		ctx.fireExceptionCaught(cause);
	}
	
	public void shutdown(EventExecutor eventExecutor) {
		if(heartBeat != null) {
			heartBeat.cancel(true);
			heartBeat = null;
		}
		
		//如果不是使用的IOEventLoop 就把这个也关了。
		if(eventExecutor instanceof DefaultEventExecutor) {
			eventExecutor.shutdownGracefully();
		}
	}
	
	private class HeartBeatTask implements Runnable{
		private final ChannelHandlerContext ctx;

		public HeartBeatTask(ChannelHandlerContext ctx) {
			super();
			this.ctx = ctx;
		}

		@Override
		public void run() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(ctx.channel().isActive()) {
				NettyMessage<Void> heatBeat = buildHeartBeat();
				System.out.println(sdf.format(new Date()) + ":Client send heart beat message to Server : "
						+ heatBeat);
				ctx.writeAndFlush(heatBeat);
			} else {
				//channel 已经不是激活状态了，那么就关闭这个连接，不要再发东西了。
				shutdown(ctx.executor());
			}
		}
		
		private NettyMessage<Void> buildHeartBeat(){
			return PING_MESSAGE;
		}
		
	}

}
