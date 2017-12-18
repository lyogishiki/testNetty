package netty2.chapter12;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class ChatServerInitializer extends io.netty.channel.ChannelInitializer<Channel>{

	private final ChannelGroup group;
	
	public ChatServerInitializer(ChannelGroup group) {
		super();
		this.group = group;
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		
		pipeline.addLast(new HttpServerCodec())
			.addLast(new ChunkedWriteHandler())
			.addLast(new HttpObjectAggregator(64 * 1024))
			.addLast(new HttpRequestHandler("/ws"))
			.addLast(new WebSocketServerProtocolHandler("/ws"))
			.addLast(new TextWebSocketFramehandler(group))
			.addLast(new CloseWebSocketFrameHandler(group));
	}

}
