package netty2.chapter12;

import java.nio.charset.StandardCharsets;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;

public class CloseWebSocketFrameHandler extends SimpleChannelInboundHandler<CloseWebSocketFrame>{

	private final ChannelGroup group;
	
	public CloseWebSocketFrameHandler(ChannelGroup group) {
		super();
		this.group = group;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, CloseWebSocketFrame msg) throws Exception {
		System.out.println(msg.statusCode() + msg.content().toString(StandardCharsets.UTF_8));
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		System.out.println(evt);
		
	}

	

	
}
