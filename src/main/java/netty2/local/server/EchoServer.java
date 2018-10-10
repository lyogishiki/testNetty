package netty2.local.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.local.LocalAddress;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalServerChannel;

public class EchoServer {


	public EchoServer() {
		super();
	}

	public static void main(String[] args) throws InterruptedException {

		new EchoServer().start();
	}

	public void start() throws InterruptedException {
		final EchoServerHandler serverHandler = new EchoServerHandler();

		// 接收新链接
		EventLoopGroup bossGroup = new DefaultEventLoopGroup(1);
		// 处理新链接
		EventLoopGroup workGroup = new DefaultEventLoopGroup(2);

		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workGroup) //
					.channel(LocalServerChannel.class)
					.childHandler(new ChannelInitializer<LocalChannel>() {
						@Override
						protected void initChannel(LocalChannel ch) throws Exception {
							// 无状态的ChannelHandler可以被标注为@Shareable，被标注为@Shareable的
							// ChannelHandler总是可以使用同样的实例。
							ch.pipeline().addLast(serverHandler)
							 .addLast(new EchoServerOutHandler());
						}
					});

			LocalAddress address = new LocalAddress("ghost");
			
			// sync 阻塞等待知道绑定完成
			ChannelFuture future = bootstrap.bind(address)
					.addListener(f -> {
						System.out.println("成功绑定!");
					}).sync();

			// System.out.println(future.channel().getClass());
			// ByteBufAllocator allocator = future.channel().alloc();
			//
			// ByteBuf buf = allocator.buffer(1024);
			//
			// System.out.println("refCnt:"+buf.refCnt());
			// buf.release();
			// System.out.println("refCnt:"+buf.refCnt());
			//
			//// buf.writeInt(1);
			//
			// System.out.println(allocator.getClass());
			// 获取closeFuture 并等待完成
			future.channel().closeFuture().sync(); //

		} finally {
			bossGroup.shutdownGracefully().sync();
			workGroup.shutdownGracefully().sync();
		}
	}
}
