package netty2.chapter11.http;

import java.io.File;

import com.sun.org.apache.bcel.internal.generic.NEW;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.stream.ChunkedNioFile;
import io.netty.handler.stream.ChunkedWriteHandler;

public class ChunkedWriteHandlerInitializer extends ChannelInitializer<Channel> {

	private final File file;
	private final SslContext sslCtx;
	
	public ChunkedWriteHandlerInitializer(File file, SslContext sslCtx) {
		super();
		this.file = file;
		this.sslCtx = sslCtx;
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ch.pipeline()
			.addLast(sslCtx.newHandler(ch.alloc()))
			//添加CHunkedWriteHandler处理传入的ChunkedInput
			.addLast(new ChunkedWriteHandler())
			.addLast(new WriteStreamHandler());
	}
	
	public final class WriteStreamHandler extends
	 ChannelInboundHandlerAdapter{

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {

			super.channelActive(ctx);
			
			ctx.writeAndFlush(
					new ChunkedNioFile(file));
		}
		
	}
	
}
