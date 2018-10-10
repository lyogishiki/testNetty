package netty2.xml;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;

public class HttpXmlResponseEncoder extends 
AbstractHttpXmlEncoder<HttpXmlResponse>{

	@Override
	protected void encode(ChannelHandlerContext ctx,
			HttpXmlResponse msg, List<Object> out) throws Exception {
		
		ByteBuf body = encode0(ctx, msg.getResult());
		
		FullHttpResponse response = msg.getResponse();
		
		if(response == null) {
			response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,body);
		} else {
			response = new DefaultFullHttpResponse(
					msg.getResponse().protocolVersion(), msg.getResponse().status(),body);
		}
		
		response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/json,text/xml");
		HttpUtil.setContentLength(response, body.readableBytes());
		
		out.add(response);
		
	}

}
