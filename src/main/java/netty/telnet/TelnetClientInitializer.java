package netty.telnet;

import netty.Consts;

import com.sun.corba.se.impl.orbutil.closure.Constant;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.ssl.SslContext;

public class TelnetClientInitializer extends ChannelInitializer<SocketChannel> {

//	private static final StringDecoder DECODER = new StringDecoder();
//	private static final StringEncoder ENCODER = new StringEncoder();
//	
//	private static final TelnetClientHandler CIENT_HANDLER = new TelnetClientHandler();
	
	private final SslContext sslCtx;

	public TelnetClientInitializer(SslContext sslCtx) {
		super();
		this.sslCtx = sslCtx;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		if(sslCtx != null){
			pipeline.addLast(sslCtx.newHandler(ch.alloc(), Consts.HOST, Consts.PORT));
		}
		
		pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()))
			.addLast(new StringDecoder())
			.addLast(new StringEncoder())
			.addLast(new TelnetClientHandler());
	}
	
	
	
}
