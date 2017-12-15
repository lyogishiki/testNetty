package netty2.chapter11.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.junit.Test;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelProgressiveFuture;
import io.netty.channel.ChannelProgressiveFutureListener;
import io.netty.channel.ChannelProgressivePromise;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.FileRegion;
//可以利用io.netty.channel.ChannelProgressivePromise来获取实时传输进度
public class TestFileRegion {
	
	@Test
	public void test01() throws FileNotFoundException {
		File file = new File("");
		FileInputStream in = new FileInputStream(file);
		
		FileRegion region = 
				new DefaultFileRegion(in.getChannel(), 0, file.length());
		
		Channel ch = null;
		ch.writeAndFlush(region)
		.addListener(new ChannelProgressiveFutureListener() {
			
			@Override
			public void operationComplete(ChannelProgressiveFuture future) throws Exception {
				
			}
			
			@Override
			public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) throws Exception {
				
			}
		});
	}
}
