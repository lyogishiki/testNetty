package netty.websocketx.server;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.util.Locale;
import java.util.logging.Level;

public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        // ping and pong frames already handled

        if (frame instanceof TextWebSocketFrame) {
            // Send the uppercase string back.
            String request = ((TextWebSocketFrame) frame).text();
           System.err.println(request);
            ctx.channel().writeAndFlush(new TextWebSocketFrame(request.toUpperCase(Locale.US)));
        } else {
            String message = "unsupported frame type: " + frame.getClass().getName();
            throw new UnsupportedOperationException(message);
        }
        
        
    }
    
    
    private void handleWebSocketFrame(ChannelHandlerContext ctx,
    	    WebSocketFrame frame) {

    	// 判断是否是Ping消息
    	if (frame instanceof PingWebSocketFrame) {
    	    ctx.channel().write(
    		    new PongWebSocketFrame(frame.content().retain()));
    	    return;
    	}
    	// 本例程仅支持文本消息，不支持二进制消息
    	if (!(frame instanceof TextWebSocketFrame)) {
    	    throw new UnsupportedOperationException(String.format(
    		    "%s frame types not supported", frame.getClass().getName()));
    	}

    	// 返回应答消息
    	String request = ((TextWebSocketFrame) frame).text();
    	
    	ctx.channel().write(
    		new TextWebSocketFrame(request
    			+ " , 欢迎使用Netty WebSocket服务，现在时刻："
    			+ new java.util.Date().toString()));
        }
}
