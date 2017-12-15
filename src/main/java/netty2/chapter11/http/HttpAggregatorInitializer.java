package netty2.chapter11.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpAggregatorInitializer extends ChannelInitializer<Channel>{
	
	private final boolean isClient;
	
	public HttpAggregatorInitializer(boolean isClient) {
		super();
		this.isClient = isClient;
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		//HttpClientCodec,HttpServerCodec 都是聚合编码器，不需要分别添加encoder，decoder了
		if(isClient) {
			pipeline.addLast("codec",new HttpClientCodec());
		} else {
			pipeline.addLast("codec",new HttpServerCodec());
		}
		//设置最大消息为512K
		pipeline.addLast("aggregator",
				new HttpObjectAggregator(512*1024));
	}

	
	
}
