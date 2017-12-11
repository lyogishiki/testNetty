package netty.snoop;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.cookie.ClientCookieEncoder;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.util.CharsetUtil;

import java.net.URI;
import java.net.URISyntaxException;

import javax.net.ssl.SSLException;

import netty.Consts;

public class HttpSnoopClient {

	public static void main(String[] args) throws URISyntaxException, SSLException, InterruptedException {
		URI uri = new URI(Consts.URL);
		String scheme = uri.getScheme() == null ? "http" : uri.getScheme();
		String host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
		int port = uri.getPort();

		if (port == -1) {
			if ("http".equalsIgnoreCase(scheme)) {
				port = 80;
			} else if ("https".equalsIgnoreCase(scheme)) {
				port = 443;
			}
		}

		if (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme)) {
			System.err.println("Only HTTP(S) is supported.");
			return;
		}

		final boolean ssl = "https".equalsIgnoreCase(scheme);
		final SslContext sslCtx;

		if (ssl) {
			sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
		} else {
			sslCtx = null;
		}

		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).handler(new HttpSnoopClientInitializer(sslCtx));
			Channel ch = b.connect(host, port).sync().channel();
			ByteBuf buf = Unpooled.copiedBuffer("我的信息23523412123\r\n",CharsetUtil.UTF_8);
			DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, 
					uri.getRawPath(),buf);
			//request.
			request.headers().set(HttpHeaderNames.HOST, host).set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE)
					.set(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP);

			request.headers().set(
					HttpHeaderNames.COOKIE,
					ClientCookieEncoder.STRICT.encode(new DefaultCookie("my-cookie", "foo"), new DefaultCookie(
							"another-cookie", "bar")));
			request.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
			
			request.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
			//request.headers().set(HttpHeaderNames.CONTENT_LENGTH,)
			ch.writeAndFlush(request);
//			ch.writeAndFlush("我的信息");
			ch.closeFuture().sync();
		} finally {
			group.shutdownGracefully();
		}
	}
}
