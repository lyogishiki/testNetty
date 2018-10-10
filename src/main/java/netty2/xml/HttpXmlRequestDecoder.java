package netty2.xml;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.nio.charset.StandardCharsets;
import java.util.List;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class HttpXmlRequestDecoder extends AbstractHttpXmlDecoder<FullHttpRequest>{

	public HttpXmlRequestDecoder(Class<?> clazz) {
		super(clazz);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, FullHttpRequest msg, List<Object> out) throws Exception {
		if(!msg.decoderResult().isSuccess()) {
			sendError(ctx,HttpResponseStatus.BAD_REQUEST);
			return;
		}
		
		HttpXmlRequest request = new HttpXmlRequest(msg	, decode0(ctx, msg.content()));
		out.add(request);
	}

	private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
		FullHttpResponse response = new DefaultFullHttpResponse(
				HttpVersion.HTTP_1_1, status,Unpooled.copiedBuffer("Failure:" + status.toString()
						+ "\r\n",
						StandardCharsets.UTF_8));
		response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain;charset=UTF-8");
		
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

}
