package netty.cors;

import java.util.Set;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.spdy.SpdyHeaders.HttpNames;
import io.netty.util.CharsetUtil;

public class OkResponseHandler extends SimpleChannelInboundHandler<Object> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println(msg.getClass());
		FullHttpRequest req = (FullHttpRequest) msg;
		String cookieStr = req.headers().get(HttpHeaderNames.COOKIE);
		Set<Cookie> cookies = ServerCookieDecoder.STRICT.decode(cookieStr);
		for(Cookie c : cookies){
			System.out.println(c.name());
			System.err.println(c.value());
			System.out.println(c);
		}
		FullHttpResponse response = new DefaultFullHttpResponse(
				HttpVersion.HTTP_1_1, HttpResponseStatus.OK,Unpooled.copiedBuffer("Hello",CharsetUtil.UTF_8));
		
		response.headers().set("custom-response-header", "Some value");
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

}
