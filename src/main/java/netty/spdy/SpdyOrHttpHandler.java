package netty.spdy;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.spdy.SpdyFrameCodec;
import io.netty.handler.codec.spdy.SpdyFrameDecoder;
import io.netty.handler.codec.spdy.SpdyHttpDecoder;
import io.netty.handler.codec.spdy.SpdyHttpEncoder;
import io.netty.handler.codec.spdy.SpdyHttpResponseStreamIdHandler;
import io.netty.handler.codec.spdy.SpdySessionHandler;
import io.netty.handler.codec.spdy.SpdyVersion;
import io.netty.handler.ssl.ApplicationProtocolConfig.Protocol;
import io.netty.handler.ssl.ApplicationProtocolNames;
import io.netty.handler.ssl.ApplicationProtocolNegotiationHandler;

public class SpdyOrHttpHandler extends ApplicationProtocolNegotiationHandler {
	private static final int MAX_CONTENT_LENGTH = 1024 * 100;

	protected SpdyOrHttpHandler() {
		super(ApplicationProtocolNames.HTTP_1_1);
	}

	@Override
	protected void configurePipeline(ChannelHandlerContext ctx, String protocol) throws Exception {
		if(ApplicationProtocolNames.SPDY_3_1.equals(protocol)){
			configureSpdy(ctx,SpdyVersion.SPDY_3_1);
		}else if(ApplicationProtocolNames.HTTP_1_1.equals(protocol)){
			configureHttp1(ctx);
		}else if(ApplicationProtocolNames.HTTP_2.equals(protocol)){
			System.err.println("-----" + protocol);
		}else{
			throw new IllegalStateException("unknown protocol: " + protocol);
		}
	}

	private void configureHttp1(ChannelHandlerContext ctx) {
		ChannelPipeline pipeline = ctx.pipeline();
		pipeline.addLast(new HttpServerCodec())
			.addLast(new HttpObjectAggregator(MAX_CONTENT_LENGTH))
			.addLast(new SpdyServerHandler());
	}

	private void configureSpdy(ChannelHandlerContext ctx, SpdyVersion version) {
		ChannelPipeline pipeline = ctx.pipeline();
		pipeline.addLast(new SpdyFrameCodec(version))
			.addLast(new SpdySessionHandler(version, true))
			.addLast(new SpdyHttpEncoder(version))
			.addLast(new SpdyHttpDecoder(version, MAX_CONTENT_LENGTH))
			.addLast(new SpdyHttpResponseStreamIdHandler())
			.addLast(new SpdyServerHandler());
	}

	
}
