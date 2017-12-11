package netty.snoop;

import java.util.PrimitiveIterator.OfDouble;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.CharsetUtil;

public class HttpSnoopClientHandler extends SimpleChannelInboundHandler<HttpObject> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
		if(msg instanceof HttpResponse){
			HttpResponse response = (HttpResponse) msg;
			System.out.println("STATUS : " + response.status());
			System.out.println("VERSION : " + response.protocolVersion());
			System.out.println();
			
			if(!response.headers().isEmpty()){
				for(String name : response.headers().names()){
					for(String value : response.headers().getAll(name)){
						System.out.println("HEADER : " + name + "=" + value);
					}
				}
				System.out.println();
			}
			
			if(HttpUtil.isTransferEncodingChunked(response)){
				System.out.println("CHUNKLED CONTENT {");
			}else{
				System.out.println("CONTENT {");
			}
		}else if(msg instanceof HttpContent){
			HttpContent content = (HttpContent) msg;
			
			System.out.print(content.content().toString(CharsetUtil.UTF_8));
			System.out.flush();
			
			if(msg instanceof LastHttpContent){
				System.out.println("} END OF CONTENT");
				ctx.close();
			}
		}else{
			System.err.println("未知的msg类型"+msg.getClass());
		}
		
	}
	
	
	 @Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
	    cause.printStackTrace();
	    ctx.close();
	}

}
