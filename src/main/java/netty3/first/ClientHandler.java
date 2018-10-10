package netty3.first;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import io.netty.util.back.ReferenceCountUtil;
import io.netty.util.concurrent.EventExecutor;

public class ClientHandler extends ChannelInboundHandlerAdapter{

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("客户端读取数据。");
		
		EventExecutor executor = ctx.executor();
		
		System.out.println(executor);
		try {
			System.out.println(ctx.channel().attr(AttributeKey.valueOf("key")));
			System.out.println(ctx.channel().attr(AttributeKey.valueOf("key")).get());
			if(msg instanceof ByteBuf) {
				ByteBuf buf = (ByteBuf) msg;
				int len = buf.readableBytes();
				byte[] bytes = new byte[len];
				buf.readBytes(bytes);
				
				System.out.println(new String(bytes));
			}else {
				System.out.println("not byteBuf" + msg);
			}
			
		} finally {
			ReferenceCountUtil.release(msg);
//			ctx.close();
		}
	}

	
}
