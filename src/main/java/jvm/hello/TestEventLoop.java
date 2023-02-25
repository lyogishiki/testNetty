package jvm.hello;

import java.nio.charset.StandardCharsets;

import org.jboss.marshalling.BytePipe;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

public class TestEventLoop {

	public static void main(String[] args) {
		// 处理IO事件,普通任务,定时任务.
		NioEventLoopGroup group1 = new NioEventLoopGroup();
		
		// 不能处理IO事件,可以处理普通任务,定时任务.
		EventLoopGroup group2 = new DefaultEventLoop();
		// group1.scheduleAtFixedRate(null, 0, 0, null)
		byte[] bytes = "您好吧".getBytes(StandardCharsets.UTF_8);
		ByteBuf buf = Unpooled.wrappedBuffer(bytes);
		System.out.println(buf.hasArray());
		System.out.println(buf.array().length);
		System.out.println(new String(buf.array(),StandardCharsets.UTF_8));
		bytes[0] = 66;
		System.out.println(buf.toString(StandardCharsets.UTF_8));
	}
}
