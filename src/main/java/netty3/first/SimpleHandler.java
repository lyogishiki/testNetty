package netty3.first;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.back.ReferenceCountUtil;

public class SimpleHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("开始读取数据====================");
		try {
			if (msg instanceof ByteBuf) {
				ByteBuf buf = (ByteBuf) msg;
				// int length = buf.readableBytes();
				// byte[] bytes = new byte[length];
				// buf.readBytes(bytes);
				// System.out.println(new String(bytes));

				String tmpMsg = buf.toString(StandardCharsets.UTF_8);
				System.out.println(tmpMsg);
			}
			System.out.println(msg);
//			
			ctx.writeAndFlush(Unpooled.copiedBuffer("Hi,return back! EventLoop线程 \r\n", StandardCharsets.UTF_8));
			
			new Thread(() -> {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("isWritable:" + ctx.channel().isWritable());
				System.out.println("isOpen:" +ctx.channel().isOpen());
				System.out.println("isRegistered:" +ctx.channel().isRegistered());
				System.out.println("isActive:" +ctx.channel().isActive());
				System.out.println("isRemoved:" + ctx.isRemoved());
				if(ctx.channel().isWritable()) {
					ctx.writeAndFlush(Unpooled.copiedBuffer("Hi,return back! 内部线程 \r\n", StandardCharsets.UTF_8));
					ctx.close();
				}
			}) .start();
			
		} finally {
			ReferenceCountUtil.release(msg);
		}

	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}
