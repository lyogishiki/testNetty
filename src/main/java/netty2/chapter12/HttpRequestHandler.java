package netty2.chapter12;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.DefaultLastHttpContent;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	private final String wsUri;
	private static final File INDEX;

	static {
		URL location = HttpRequestHandler.class.getProtectionDomain().getCodeSource().getLocation();

		try {
			String path = location.toURI() + "index.html";
			path = !path.startsWith("file:") ? path : path.substring(5);
			INDEX = new File(path);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			throw new IllegalStateException("" + "unable to locate index.html", e);
		}
	}

	public HttpRequestHandler(String wsUri) {
		super();
		this.wsUri = wsUri;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		// 如果请求了WebSocket协议升级，则增加引用计数（调用retain方法），并传给下一个
		// 因为SimpleChannelInbound的channelRead方法调用完成之后，将会调用ReferenceUtil.release
//		方法释放这个资源，所以一定要调用retain方法，避免释放
		if (wsUri.equalsIgnoreCase(request.uri())) {
			ctx.fireChannelRead(request.retain());
			return;
		}

		// 处理100Continue请求，以符合HTTP 1.1规范
//		如果客户端发送100-continental，则发送一个100continue响应.
		if (HttpUtil.is100ContinueExpected(request)) {
			send100Continue(ctx);
		}
		
		
		RandomAccessFile file = new RandomAccessFile(INDEX, "r");

		HttpResponse response = new DefaultHttpResponse(request.protocolVersion(), HttpResponseStatus.OK);

		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");

		boolean keepAlive = HttpUtil.isKeepAlive(request);
		// 如果请求了keep-alive，则添加所需要的HTTP头信息。
		if (keepAlive) {
			response.headers().set(HttpHeaderNames.CONTENT_LENGTH, file.length());
			response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
		}

//		在改Http头信息被设置之后，HttpRequestHandler将会写会一个HTTPResponse给客户端，这不是一个FullHttpResponse，
		// 将HTTPResponse写到客户端
		ctx.write(response);

		// 将index.html写到客户端
		if (ctx.pipeline().get(SslHandler.class) == null) {
			// 如果是没有ssl加密的情况，直接使用0拷贝的FileRegion。
//			最佳效率
			ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
		} else {
			// 使用了SSL加密的情况，使用ChunkedFile发送，占用少量内存发送大文件
			ctx.write(new ChunkedNioFile(file.getChannel()));
		}

		// 写LastHTTPContent 并冲刷到客户端。
		ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);

		// 如果没有请求keep-alive，则在写操作完成之后关闭Channel
		if (!keepAlive) {
			future.addListener(ChannelFutureListener.CLOSE);
		}

		file.close();

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	// 发送Continue FullHTTPResponse
	private static final void send100Continue(ChannelHandlerContext ctx) {
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
		ctx.writeAndFlush(response);
	}

}
