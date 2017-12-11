package netty.spdy.client;

import javax.net.ssl.SSLException;

import netty.Consts;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.ssl.ApplicationProtocolConfig;
import io.netty.handler.ssl.ApplicationProtocolConfig.SelectedListenerFailureBehavior;
import io.netty.handler.ssl.ApplicationProtocolConfig.SelectorFailureBehavior;
import io.netty.handler.ssl.ApplicationProtocolNames;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.ApplicationProtocolConfig.Protocol;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

public class SpdyClient {

	public static void main(String[] args) throws Exception {
		final SslContext sslCtx = SslContextBuilder
				.forClient()
				.trustManager(InsecureTrustManagerFactory.INSTANCE)
				.applicationProtocolConfig(
						new ApplicationProtocolConfig(Protocol.NPN, SelectorFailureBehavior.NO_ADVERTISE,
								SelectedListenerFailureBehavior.ACCEPT, ApplicationProtocolNames.SPDY_3_1,
								ApplicationProtocolNames.HTTP_1_1, ApplicationProtocolNames.HTTP_2)).build();

		HttpResponseClientHandler httpResponseHandler = new HttpResponseClientHandler();
		EventLoopGroup workGroup = new NioEventLoopGroup();

		try {
			Bootstrap b = new Bootstrap();
			b.group(workGroup).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true)
					.handler(new SpdyClientInitializer(sslCtx, httpResponseHandler));
			Channel ch = b.connect(Consts.HOST, Consts.PORT).sync().channel();
			HttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "");
			request.headers().set(HttpHeaderNames.HOST, Consts.HOST);
			request.headers().set(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP);
			ch.writeAndFlush(request).sync();
			httpResponseHandler.queue.take().sync();
			System.out.println("Finished SPDY HTTP GET");
			ch.close().syncUninterruptibly();
		} finally {
			workGroup.shutdownGracefully();
		}
	}
}
