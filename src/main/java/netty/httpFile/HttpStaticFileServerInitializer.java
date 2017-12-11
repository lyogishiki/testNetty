package netty.httpFile;

import io.netty.channel.socket.SocketChannel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.stream.ChunkedWriteHandler;

public class HttpStaticFileServerInitializer extends ChannelInitializer<SocketChannel> {

	private final SslContext sslCtx;
	
	public HttpStaticFileServerInitializer(SslContext sslCtx) {
		super();
		this.sslCtx = sslCtx;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		
		if(sslCtx != null){
			pipeline.addLast(sslCtx.newHandler(ch.alloc()));
		}
		
		pipeline.addLast(new HttpServerCodec())
			.addLast(new HttpObjectAggregator(65536))
			.addLast(new ChunkedWriteHandler())
			.addLast(new HttpStaticFileServerHandler());
		
	}
	

}
