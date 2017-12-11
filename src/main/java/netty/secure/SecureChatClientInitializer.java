package netty.secure;

import netty.Consts;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;

public class SecureChatClientInitializer extends ChannelInitializer<SocketChannel> {

	private final SslContext sslCtx;
	
	public SecureChatClientInitializer(SslContext sslCtx) {
		super();
		this.sslCtx = sslCtx;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		
		pipeline.addLast(sslCtx.newHandler(ch.alloc(),Consts.HOST,Consts.PORT));
		pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()))
			.addLast(new StringDecoder())
			.addLast(new StringEncoder())
			.addLast(new SecureChatClientHandler());
		
		
	}

}
