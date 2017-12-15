package netty2.chapter11.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class HttpPipelineInitializer extends ChannelInitializer<Channel>{

	private final boolean client;
	
	public HttpPipelineInitializer(boolean client) {
		super();
		this.client = client;
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		
		if(client) {
			//客户端添加HttpResponseDecoder 将服务点返回的字节解码为HTTPResponse
			//客户端添加HttpRequestEncoder ，将发送个服务端的信息编码为字节码
			pipeline.addLast("decoder"
					,new HttpResponseDecoder())
			.addLast("encoder",new HttpRequestEncoder());
				
		} else {
			//将客户端发送的字节解码为HTTPRequest
			pipeline.addLast("decoder",new HttpRequestDecoder())
			//将返回给客户端的HttpResponse编码为字节
				.addLast("encoder",new HttpResponseEncoder());
		}
	}

	
	
}
