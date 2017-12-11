package netty.secure;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.ssl.SslContext;

public class SecureChatServerInitializer extends ChannelInitializer<SocketChannel> {

	private final SslContext sslCtx;
	
	public SecureChatServerInitializer(SslContext sslCtx) {
		super();
		this.sslCtx = sslCtx;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(sslCtx.newHandler(ch.alloc()))
			.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()))
			.addLast(new StringDecoder())
			.addLast(new StringEncoder())
			.addLast(new SecureChatServerHander());
				
	}
	
}
