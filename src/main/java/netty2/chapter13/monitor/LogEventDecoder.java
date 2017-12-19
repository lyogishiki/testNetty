package netty2.chapter13.monitor;

import java.nio.charset.StandardCharsets;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import netty2.chapter13.LogEvent;

public class LogEventDecoder extends MessageToMessageDecoder<DatagramPacket>{

	@Override
	protected void decode(ChannelHandlerContext ctx, DatagramPacket datagramPacket, List<Object> out) throws Exception {
		
		ByteBuf data = datagramPacket.content();
		//获取分隔符索引
		int index = data.indexOf(0, data.readableBytes(), LogEvent.SEPARATOR);
		
		String fileName = data.slice(0, index).toString(
				StandardCharsets.UTF_8);
		String msg = data.slice(index+1, data.readableBytes())
				.toString(StandardCharsets.UTF_8);
		
		LogEvent event = new LogEvent(datagramPacket.sender(), System.currentTimeMillis(), 
				fileName, msg);
		
		out.add(event);
		
	}

	
	
}
