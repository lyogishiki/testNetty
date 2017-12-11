package netty.snoop;

import java.nio.charset.Charset;
import java.util.PrimitiveIterator.OfDouble;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.CharsetUtil;

public class HttpSnoopClientHandler1 extends SimpleChannelInboundHandler<FullHttpResponse> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse response) throws Exception {
		
		System.out.println("STATUS : " + response.status());
		System.out.println("VERSION : " + response.protocolVersion());
		
		if(!response.headers().isEmpty()){
			for(String name : response.headers().names()){
				for(String value : response.headers().getAll(name)){
					System.out.println("HEADER : " + name + "=" + value);
				}
			}
		}
		
		if(HttpUtil.isTransferEncodingChunked(response)){
			System.out.println("CHUNKLED CONTENT { ");
		}else{
			System.out.println("CONTENT{");
		}
		
		System.out.println(response.content().toString(CharsetUtil.UTF_8));
		
		System.out.println("} END OF CONTENT");
		ctx.close();
	
	}
	
	
	 @Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
	    cause.printStackTrace();
	    ctx.close();
	}

}
