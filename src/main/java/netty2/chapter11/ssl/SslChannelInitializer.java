package netty2.chapter11.ssl;

import javax.net.ssl.SSLEngine;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

/**
 * 大多数情况下，SslHandler将是ChannelPipeline中的第一个CHannelHandler
 * 确保其他的都是经过解密后的数据
 * @author Administrator
 *
 */
public class SslChannelInitializer extends ChannelInitializer<Channel>{
	
	//要是用的SSLContext
	private final SslContext context;
	//如果设置为true，则第一个写入的消息将不会被加密，客户端应设置为true
	private final boolean startTls;
	
	public SslChannelInitializer(SslContext context, boolean startTls) {
		super();
		this.context = context;
		this.startTls = startTls;
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {
		//使用Channel的ByteBufAllocator获取一个新的SSLEngine
		SSLEngine engine = context.newEngine(ch.alloc());
		SslHandler sslHandler = new SslHandler(engine,startTls);
		ch.pipeline().addFirst("ssl",
				sslHandler);
//		设置超时时间，超时之后，握手ChannelFture将会被通知失败
//		sslHandler.setHandshakeTimeout(handshakeTimeout, unit);
//		设置超时时间，超时之后将会出发一个关闭通知并关闭连接，这也将会导致ChannelFUture失败
//		sslHandler.setCloseNotifyFlushTimeout(closeNotifyFlushTimeout, unit);(closeNotifyTimeout, unit);
	}

}
