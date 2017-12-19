package netty2.chapter13;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;

public class LogEventEncoder extends MessageToMessageEncoder<LogEvent>{

	private final InetSocketAddress remoteAddress;
	
	public LogEventEncoder(InetSocketAddress remoteAddress) {
		super();
		this.remoteAddress = remoteAddress;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, LogEvent logEvent, List<Object> out) throws Exception {
		byte[] file = logEvent.getLogfile().getBytes(StandardCharsets.UTF_8);
		byte[] msg = logEvent.getMsg().getBytes(StandardCharsets.UTF_8);
		
		//分配一个file msg 分隔符: 大小的buffer
		ByteBuf buf = ctx.alloc().buffer(file.length + msg.length + 1);
		
		buf.writeBytes(file);
		buf.writeByte(LogEvent.SEPARATOR);
		buf.writeBytes(msg);
		
		//写入一个拥有数据和目的地址的新DatagramPacket添加到出战的消息列表中。
		out.add(new DatagramPacket(buf, remoteAddress));
	}


	

	
	
}
