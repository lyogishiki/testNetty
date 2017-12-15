package netty2.chapter11.http;

import javax.net.ssl.SSLEngine;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

public class HttpCodecInitializer extends ChannelInitializer<Channel>{

	private final SslContext context;
	private final boolean isClient;
	
	public HttpCodecInitializer(SslContext context, boolean isClient) {
		super();
		this.context = context;
		this.isClient = isClient;
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		
		/*SSLEngine engine = context.newEngine(ch.alloc());
		SslHandler handler = new SslHandler(engine);
		*/
		
		SslHandler handler = context.newHandler(ch.alloc());
		pipeline.addLast("ssl",handler);
		
		if(isClient) {
			pipeline.addLast("codec",new HttpClientCodec());
		}else {
			pipeline.addLast("codec",new HttpServerCodec());
		}
	}

}
