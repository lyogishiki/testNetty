package netty.cors;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.cors.CorsConfig;
import io.netty.handler.codec.http.cors.CorsConfigBuilder;
import io.netty.handler.codec.http.cors.CorsHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.stream.ChunkedWriteHandler;

public class HttpCorsServerInitializer extends ChannelInitializer<SocketChannel> {

	private final SslContext sslCtx;
	
	public HttpCorsServerInitializer(SslContext sslCtx) {
		super();
		this.sslCtx = sslCtx;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
//		CorsConfigBuilder.forAnyOrigin().allowNullOrigin().allowCredentials().build();
		CorsConfig corsConfig = CorsConfigBuilder.forAnyOrigin().
				allowNullOrigin().allowCredentials().build();
		ChannelPipeline pipeline = ch.pipeline();
		if(sslCtx != null){
			pipeline.addLast(sslCtx.newHandler(ch.alloc()));
		}
		
		pipeline.addLast(new HttpResponseEncoder())
			.addLast(new HttpRequestDecoder())
			.addLast(new HttpObjectAggregator(65536))
			.addLast(new ChunkedWriteHandler())
			.addLast(new CorsHandler(corsConfig))
			.addLast(new OkResponseHandler());
	}

}
