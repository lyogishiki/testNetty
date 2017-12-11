package netty.discard;

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

public class UptimeClientHandler extends SimpleChannelInboundHandler<Object> {

	long startTime = -1;

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("read0 " + msg);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		println("sleeping for " + UptimeClient.RECONNECT_DELAY + "s");
		final EventLoop loop = ctx.channel().eventLoop();
		loop.schedule(new Runnable() {

			@Override
			public void run() {
				println("Reconnecting to: " + UptimeClient.HOST + ':' + UptimeClient.PORT);
				UptimeClient.connect(UptimeClient.configureBootstrap(new Bootstrap(), loop));
			}
		}, UptimeClient.RECONNECT_DELAY, TimeUnit.SECONDS);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		if (startTime < 0) {
			startTime = System.currentTimeMillis();
		}
		println("connect to " + ctx.channel().remoteAddress());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		println("Disconnected from : " + ctx.channel().remoteAddress());
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (!(evt instanceof IdleStateEvent)) {
			return;
		}
		IdleStateEvent e = (IdleStateEvent) evt;
		if (e.state() == IdleState.READER_IDLE) {
			println("Disconnecting due to no inbound traffic");
			ctx.close();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	void println(String msg) {
		if (startTime < 0) {
			System.err.format("[SERVER IS DOWN] %s%n", msg);
		} else {
			System.err.format("[UPTIME: %5ds] %s%n", (System.currentTimeMillis() - startTime) / 1000, msg);
		}
	}
}
