package netty.worldclock;

import netty.Consts;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.ssl.SslContext;

public class WorldClockClientInitializer extends ChannelInitializer<SocketChannel> {
	
	private final SslContext sslCtx;
	
	public WorldClockClientInitializer(SslContext sslCtx) {
		super();
		this.sslCtx = sslCtx;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
		
		p.addLast(sslCtx.newHandler(ch.alloc(),Consts.HOST,Consts.PORT))
			.addLast(new ProtobufVarint32FrameDecoder())
			.addLast(new ProtobufDecoder(WorldClockProtocol.LocalTimes.getDefaultInstance()))
			.addLast(new ProtobufVarint32LengthFieldPrepender())
			.addLast(new ProtobufEncoder())
			.addLast(new WorldClockClientHandler());
		
	}

}
