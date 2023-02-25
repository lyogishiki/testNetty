package jvm.nio;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOption;

public class QuitHandler extends ChannelInboundHandlerAdapter{

	/**
	 * 连接断开时触发.
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		FileInputStream fis = new FileInputStream(null);
		FileChannel fc = fis.getChannel();
//		fc.read(null)
		ByteBuffer buf = ByteBuffer.allocate(0)
//		buf.write
	}

	
	
}
