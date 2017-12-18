package netty2.chapter12;

import javax.net.ssl.SSLEngine;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

public class SecureChatServerinitializer extends ChatServerInitializer{

	private final SslContext context;
	
	public SecureChatServerinitializer(ChannelGroup group,SslContext context) {
		super(group);
		this.context = context;
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {
		super.initChannel(ch);
		
		SslHandler sslHandler = context.newHandler(ch.alloc());
		sslHandler.engine().setUseClientMode(false);
		
		ch.pipeline().addFirst(sslHandler);
		
	}


}
