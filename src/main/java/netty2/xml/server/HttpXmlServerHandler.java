package netty2.xml.server;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import netty.http.pojo.Order;
import netty.http.pojo.OrderFactory;
import netty2.xml.HttpXmlRequest;
import netty2.xml.HttpXmlResponse;

public class HttpXmlServerHandler extends SimpleChannelInboundHandler<HttpXmlRequest> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpXmlRequest msg) throws Exception {
		HttpRequest request = msg.getRequest();
		Order order = (Order) msg.getBody();
		
		System.out.println("http Server receive msg : " + order);
		
		Order content = dobusiness();
		
		ChannelFuture future = ctx.writeAndFlush(new HttpXmlResponse(null, content));
			
		if(!HttpUtil.isKeepAlive(request)) {
			future.addListener(ChannelFutureListener.CLOSE);
		}
	}

	private Order dobusiness() {
		return OrderFactory.create(512);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		if(ctx.channel().isActive()) {
			sendError(ctx,HttpResponseStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
		FullHttpResponse response = new DefaultFullHttpResponse
				(HttpVersion.HTTP_1_1, status,
						Unpooled.copiedBuffer(status.toString()+"\r\n", 
								StandardCharsets.UTF_8));
		response.headers().set(HttpHeaderNames.CONTENT_TYPE,
				"text/plain;charset=UTF-8");
		
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}
	
	

}
