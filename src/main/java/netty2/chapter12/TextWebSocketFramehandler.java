package netty2.chapter12;

import com.sun.org.apache.bcel.internal.generic.NEW;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler.HandshakeComplete;

public class TextWebSocketFramehandler extends SimpleChannelInboundHandler<TextWebSocketFrame>{
	
	private final ChannelGroup group;

	public TextWebSocketFramehandler(ChannelGroup group) {
		super();
		this.group = group;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
//		增加消息的引用计数，并将她写到ChannelGroup中所有已连接的客户端
//		因为SimpleChannelInbound的ChannelchannelRead方法结束时会自动回收这条msg，而且WriteAndFlush可能会在channelRead0之后
//		发送数据，所以一定要调用retain方法增加计数，否则报错。
		group.writeAndFlush(msg.retain());
	}

//	处理用户自定义事件。
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		//如果该事件，则表示握手成功，则从改ChannelPipeline中移除HTTPRequestHandler，因为将不会接收到任何Http消息了。
		if(evt instanceof HandshakeComplete) {
			ctx.pipeline().remove(HttpRequestHandler.class);
//			通知所有已经连上WebSocket客户端新的客户端已经连上。
			group.writeAndFlush(new TextWebSocketFrame(
					"Client " + ctx.channel() + " joined"));	
			group.add(ctx.channel());
		}else {
			
			super.userEventTriggered(ctx, evt);
		}
	
	}
	
	

}
