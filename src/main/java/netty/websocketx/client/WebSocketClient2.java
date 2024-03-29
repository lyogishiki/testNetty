package netty.websocketx.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class WebSocketClient2 {

	static final String URL = System.getProperty("url", "ws://127.0.0.1:8080/websocket");

	public static void main(String[] args) throws Exception {
		URI uri = new URI(URL);
		String scheme = uri.getScheme() == null ? "ws" : uri.getScheme();
		final String host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
		final int port;
		if (uri.getPort() == -1) {
			if ("ws".equalsIgnoreCase(scheme)) {
				port = 80;
			} else if ("wss".equalsIgnoreCase(scheme)) {
				port = 443;
			} else {
				port = -1;
			}
		} else {
			port = uri.getPort();
		}

		if (!"ws".equalsIgnoreCase(scheme) && !"wss".equalsIgnoreCase(scheme)) {
			System.err.println("Only WS(S) is supported.");
			return;
		}

		final boolean ssl = "wss".equalsIgnoreCase(scheme);
		final SslContext sslCtx;
		if (ssl) {
			sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
		} else {
			sslCtx = null;
		}

		EventLoopGroup group = new NioEventLoopGroup(100);

		try {
			// Connect with V13 (RFC 6455 aka HyBi-17). You can change it to V08 or V00.
			// If you change it to V00, ping is not supported and remember to change
			// HttpResponseDecoder to WebSocketHttpResponseDecoder in the pipeline.

			List<Channel> list = new ArrayList<>(10000);
			for (int i = 0; i < 10000; i++) {
				final WebSocketClientHandler handler = new WebSocketClientHandler(WebSocketClientHandshakerFactory
						.newHandshaker(uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders()));

				Bootstrap b = new Bootstrap();
				b.group(group)
				.channel(NioSocketChannel.class)
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 
						30000)
//				.option(ChannelOption.SO_TIMEOUT, 
//						30000)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) {
						ChannelPipeline p = ch.pipeline();
						if (sslCtx != null) {
							p.addLast(sslCtx.newHandler(ch.alloc(), host, port));
						}
						p.addLast(new HttpClientCodec(), new HttpObjectAggregator(8192),
								WebSocketClientCompressionHandler.INSTANCE, handler);
					}
				});

				Channel ch = b.connect(uri.getHost(), port)
						.addListener(	//
						c -> handler.handshakeFuture()).channel();
				list.add(ch);
				Thread.sleep(1);
			}

			// BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
			// while (true) {
			// String msg = console.readLine();
			// if (msg == null) {
			// break;
			// } else if ("bye".equals(msg.toLowerCase())) {
			// ch.writeAndFlush(new CloseWebSocketFrame());
			// ch.closeFuture().sync();
			// break;
			// } else if ("ping".equals(msg.toLowerCase())) {
			// WebSocketFrame frame = new PingWebSocketFrame(Unpooled.wrappedBuffer(new byte[] { 8, 1, 8, 1 }));
			// ch.writeAndFlush(frame);
			// } else {
			// WebSocketFrame frame = new TextWebSocketFrame(msg);
			// ch.writeAndFlush(frame);
			// }
			// }
			Thread.sleep(Long.MAX_VALUE);
		} finally {
			group.shutdownGracefully();
		}
	}

}
