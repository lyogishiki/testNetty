package netty.rumen;

import java.util.concurrent.atomic.AtomicInteger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeClientHandler extends ChannelInboundHandlerAdapter  {

	
	private final AtomicInteger ai = new AtomicInteger(0);
	
	public TimeClientHandler() {
		super();
		
	}

	//当TCP建立连接后，会调用channelActivite
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ByteBuf firstMessage;
		byte[] req = ("Query time"+System.getProperty("line.separator")).getBytes();
		for(int i=0;i<100;i++){
			firstMessage = Unpooled.buffer(req.length);
			firstMessage.writeBytes(req);
			ctx.writeAndFlush(firstMessage);
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		//ByteBuf readBuf = (ByteBuf) msg;
		//byte[] bytes = new byte[readBuf.readableBytes()];
		//readBuf.readBytes(bytes);
		//String req = new String(bytes,"UTF-8");
		String req = (String) msg;
		System.out.println(req + ",times:" + ai.incrementAndGet());
		
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
		cause.printStackTrace();
	}

	
}
