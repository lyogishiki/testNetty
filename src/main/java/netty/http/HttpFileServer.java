package netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

public class HttpFileServer {

	private static final String DEFAULT_URL = "/src";

	public void run(final int port,final String url) throws InterruptedException{
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup,workGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast("http-decoder",new HttpRequestDecoder())
							//把多个消息转为单一的fullHttpRequest或者fullHttpResponse
							//因为http解码器会把一个http消息生成多个消息对象。1，httpRequest/HttpResponse
							//2,HttpContent.3,LastHttpContent
							.addLast("http-aggregator",new HttpObjectAggregator(65536))
							//Http相应解码器，对Http相应消息解码
							.addLast("httpEncpder",new HttpResponseEncoder())
							//异步发送大的码流，但不占用过多内存
							.addLast("http-chunked",new ChunkedWriteHandler())
							.addLast("fileServerHandler",new HttpFileServerHandler(url));
					}
				});
			
			ChannelFuture future = b.bind(8080).sync();
			System.out.println("网址:http://127.0.0.1:8080/src");
			future.channel().closeFuture().sync();
		}finally{
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		int port = 8080;
		
		String url = DEFAULT_URL;
		
		new HttpFileServer().run(port, url);
	}
}
