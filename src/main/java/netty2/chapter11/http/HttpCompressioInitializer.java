package netty2.chapter11.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpCompressioInitializer extends ChannelInitializer<Channel>{

	private final boolean isClient;
	
	public HttpCompressioInitializer(boolean isClient) {
		super();
		this.isClient = isClient;
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		
		if(isClient) {
			//客户端使用HTTPContentDecompressor来解压缩从服务端来的数据
			pipeline.addLast("codec",new HttpClientCodec())
			.addLast("decompressor",
					new HttpContentDecompressor());
		} else {
			//服务器端使用HTTPContentCompressor来压缩数据
			pipeline.addLast("codec",new HttpServerCodec())
			.addLast("compressor",new HttpContentCompressor());
		}
		
		pipeline.addLast("aggregator",
				new HttpObjectAggregator(512*1024));
	}
	

}
