package netty.spdy;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;

public class SpdyServerInitializer extends ChannelInitializer<SocketChannel> {

	private final SslContext sslCtx;
	
	
	public SpdyServerInitializer(SslContext sslCtx) {
		super();
		this.sslCtx = sslCtx;
	}	
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		
		pipeline.addLast(sslCtx.newHandler(ch.alloc()));
		
		pipeline.addLast(new SpdyOrHttpHandler());
	}

}
