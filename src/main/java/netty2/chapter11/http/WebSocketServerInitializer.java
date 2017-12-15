package netty2.chapter11.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

public class WebSocketServerInitializer 
extends ChannelInitializer<Channel>{

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ch.pipeline()
		.addLast(new HttpServerCodec())
		//提供聚合的HTTPRequest
		.addLast(new HttpObjectAggregator(512*1024))
		//如果被请求的端点是"WebSocket"，则处理该升级握手
		.addLast(new WebSocketServerProtocolHandler("/websocket"))
		.addLast(new TextFrameHandler())
		.addLast(new BinaryFrameHandler())
		.addLast(new ContinutionFrameHandler());
	}

	
	public static class TextFrameHandler extends 
	SimpleChannelInboundHandler<TextWebSocketFrame>{

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
			System.out.println(msg);
		}
		
	}
	
	public static class BinaryFrameHandler extends SimpleChannelInboundHandler<BinaryWebSocketFrame>{

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, BinaryWebSocketFrame msg) throws Exception {
			System.out.println(msg);
		}
		
	}
	
	public static class ContinutionFrameHandler extends SimpleChannelInboundHandler<ContinutionFrameHandler>{

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, ContinutionFrameHandler msg) throws Exception {
			System.out.println(msg);
		}
	}
}
