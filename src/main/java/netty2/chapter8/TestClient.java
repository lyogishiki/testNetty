package netty2.chapter8;

import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.concurrent.GenericFutureListener;

public class TestClient {

	public static void main(String[] args) {
		EventLoopGroup group = new NioEventLoopGroup(1);
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group)
			.channel(NioSocketChannel.class)
			.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {

					ch.pipeline()
//							.addLast(new HttpClientCodec())
//							.addLast(new HttpContentDecompressor())
//							.addLast(new HttpObjectAggregator(512 * 1024))
							.addLast(new SimpleChannelInboundHandler<Object>() {
								@Override
								protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
									System.out.println("receive data:" + msg);
								}
							});
				}
			})
			.option(ChannelOption.SO_KEEPALIVE, 
					true)
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 
					30000);

			ChannelFuture f = bootstrap.connect(new InetSocketAddress("www.baidu.com", 80));

			f.addListener(new GenericFutureListener<ChannelFuture>() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if (future.isSuccess()) {
						System.out.println("连接成功！");
						// future.channel().write("some thing");
					} else {
						System.out.println("连接失败！");
						future.cause().printStackTrace();
					}
				}
			}).sync();
			
			URI uri = new URI("http://www.baidu.com");
			String host = "www.baidu.com";
			String msg = "Are you ok?";
			DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,
					uri.toASCIIString(), Unpooled.wrappedBuffer(msg.getBytes("UTF-8")));

			// 构建http请求
			request.headers().set(HttpHeaders.Names.HOST, host);
			request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
			request.headers().set(HttpHeaders.Names.CONTENT_LENGTH, request.content().readableBytes());
			// 发送http请求
			f.channel().write(request);

			// f.channel().close().sync();
			f.channel().close().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}
}
