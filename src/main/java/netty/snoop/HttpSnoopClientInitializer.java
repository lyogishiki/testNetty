package netty.snoop;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.ssl.SslContext;

public class HttpSnoopClientInitializer extends ChannelInitializer<SocketChannel> {

	private final SslContext sslCtx;
	
	public HttpSnoopClientInitializer(SslContext sslCtx) {
		super();
		this.sslCtx = sslCtx;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
		
		if(sslCtx != null){
			p.addLast(sslCtx.newHandler(ch.alloc()));
		}
		
		p.addLast(new HttpClientCodec());
		// Remove the following line if you don't want automatic content decompression.
		p.addLast(new HttpContentDecompressor());
		
		//Uncomment the following line if you don't want to handle httpContents
//把多个消息转为单一的fullHttpRequest或者fullHttpResponse
		//因为http解码器会把一个http消息生成多个消息对象。1，httpRequest/HttpResponse
		//2,HttpContent.3,LastHttpContent
		p.addLast(new HttpObjectAggregator(1048576));
		p.addLast(new HttpSnoopClientHandler1());
		
		
//		p.addLast(new HttpSnoopClientHandler());
	}

}
