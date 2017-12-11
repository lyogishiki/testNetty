package netty.factorial;

import netty.Consts;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.compression.ZlibCodecFactory;
import io.netty.handler.codec.compression.ZlibWrapper;
import io.netty.handler.ssl.SslContext;

public class FactoriaClientInitializer extends ChannelInitializer<SocketChannel> {

	private final SslContext sslCtx;
	
	public FactoriaClientInitializer(SslContext sslCtx) {
		super();
		this.sslCtx = sslCtx;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		
		pipeline.addLast(sslCtx.newHandler(ch.alloc(),Consts.HOST,Consts.PORT))
			//压缩
			.addLast(ZlibCodecFactory.newZlibEncoder(ZlibWrapper.GZIP))
			.addLast(ZlibCodecFactory.newZlibDecoder(ZlibWrapper.GZIP))
			.addLast(new BigIntegerDecoder())
			.addLast(new NumberEncoder())
			
			.addLast(new FactorialClientHandler());
	}

}
