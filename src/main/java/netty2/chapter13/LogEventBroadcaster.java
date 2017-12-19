package netty2.chapter13;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class LogEventBroadcaster {

	private final EventLoopGroup group;
	private final Bootstrap bootstrap;
	private final File file;
	
	public LogEventBroadcaster(InetSocketAddress address,File file) {
		super();
		group = new NioEventLoopGroup();
		bootstrap = new Bootstrap();
		
		bootstrap.group(group)
//		NioDatagramChannel 无连接的
			.channel(NioDatagramChannel.class)
//MYTASK 修正			设置SO_BROADCAST选项，允许发送广播？
			.option(ChannelOption.SO_BROADCAST, true)
			.handler(new LogEventEncoder(address));
		
		this.file = file;
		
	}
	
	public void run() throws Exception {
		Channel ch = bootstrap.bind(0).sync().channel();
		
		long pointer = 0;
		
		for(;;) {
			long len = file.length();
			
			if(len < pointer) {
				//这不是到最后，然后开始空转了？
				pointer = len;
			} else if(len > pointer){
				RandomAccessFile raf = new RandomAccessFile(file, "r");
				raf.seek(pointer);
				String line = null;
				while((line = raf.readLine()) != null) {
					ch.writeAndFlush(new LogEvent(null, -1,
							file.getAbsolutePath(),line));
				}
				//这不是已经到最后了？
				pointer = raf.getFilePointer();
				raf.close();
			}
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
				Thread.interrupted();
				break;
			}
		}
		
		ch.close().syncUninterruptibly();
	}
	
	public void stop() {
		group.shutdownGracefully();
	}
	
	public static void main(String[] args) {
		
		int port = 9090;
		String file = Thread.currentThread()
				.getContextClassLoader().getResource("index.html").getPath();
		
		LogEventBroadcaster broadcaster = 
				new LogEventBroadcaster(
						new InetSocketAddress("255.255.255.255", 
								port), new File(file));
		try {
			broadcaster.run();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			broadcaster.stop();
		}
		
//		new InetSocketAddress(hostname, port)
	}
}

