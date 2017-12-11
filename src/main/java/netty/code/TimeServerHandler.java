package netty.code;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeServerHandler extends ChannelInboundHandlerAdapter  {
	
	private AtomicInteger ai = new AtomicInteger(0);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		//ByteBuf buf = (ByteBuf) msg;
		//byte[] req = new byte[buf.readableBytes()];
		//buf.readBytes(req);
		//String body = new String(req,"UTF-8");
//		String body = (String) msg;			//经过解析，拿到的已经是String类型的数据
//		System.out.println(body + ",times : " + ai.incrementAndGet()) ;
//		SimpleDateFormat sdf = new SimpleDateFormat();
//		String currentTime = sdf.format(new Date()) + System.getProperty("line.separator");
//		ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
//		ctx.write(resp);
		//UserInfo userInfo = (UserInfo) msg;
		System.out.println(msg);
		Thread.sleep(100);
		ctx.write(msg);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		//将消息队列中的消息写入socketChannel发给对方，
		//从性能角度考虑，防止频繁唤醒selector进行消息发送，netty
		//write方法并不直接写入SocketChannel中，调用Write方法只是把代发消息放大缓冲数组，
		//在调用flush方法将缓冲区的消息全部写到socketchannel中
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	
	

}
