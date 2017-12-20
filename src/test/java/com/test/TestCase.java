package com.test;

import java.nio.charset.StandardCharsets;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.util.ByteProcessor;

public class TestCase {

	Logger logger = LoggerFactory.getLogger(TestCase.class);
	@Test
	public void test01() {
		ByteBuf buf = Unpooled.buffer(10, 20);
		logger.info(buf.getClass().toString());
		System.out.println(logger.getClass());
		buf.writeInt(1);
		buf.writeInt(2);
		buf.writeInt(3);
		buf.writeInt(4);
		buf.writeInt(5);
		buf.writeInt(6);
		
	}
	
	@Test
	public void test02() {
		char c1 = '你';
		
		System.out.println((int)Character.MIN_VALUE);
		System.out.println((int)Character.MAX_VALUE);
		for(int i=0;i<=Character.MAX_VALUE;i++) {
			logger.info(Character.toString((char)i));
		}
	}
	
	@Test
	public void test03() {
		String str = "1234567889\r\n2645645634\r\n";
		
		ByteBuf buf = Unpooled.copiedBuffer(
				str, 
				StandardCharsets.UTF_8);
		
//		buf.indexOf(0, buf.writableBytes(), value)
		System.out.println(buf.writableBytes());
		System.out.println(buf.capacity());
		char c = '\r';
		
		System.out.println(buf.indexOf(0, 48, (byte)c));
		
		int i1 = buf.forEachByte(ByteProcessor.FIND_CRLF);
		
		int i2 = buf.forEachByte(b -> {
			System.out.println(b);
			return true;
		});
		
		System.out.println("i1" + i1);
		System.out.println("i2" + i2);
	}
	
	@Test
	public void test04() {
		ByteBuf buf = Unpooled.copiedBuffer(
				"hello,Netty,welcome.", 
				StandardCharsets.UTF_8);
		
		ByteBuf sliced = buf.slice(0, 10);
		ByteBuf copied = buf.copy();
		
		System.out.println(sliced.toString(StandardCharsets.UTF_8));
		
		sliced.setByte(0, 'j');
		
		System.out.println(buf.toString(StandardCharsets.UTF_8));
		System.out.println(copied.toString(StandardCharsets.UTF_8));
		System.out.println(copied.toString(StandardCharsets.UTF_8));
		
		System.out.println(copied.readableBytes());
		System.out.println(copied.writableBytes());
		System.out.println("getChar " + copied.getChar(0));
		
		System.out.println(copied.readerIndex());
		System.out.println("readChar " + copied.readChar());
		System.out.println(copied.readerIndex());
		
		
		byte[] bytes = "he".getBytes(StandardCharsets.UTF_8);
		System.out.println(bytes);
		
		int i = 104 << 8 | 101;
		System.out.println(i);
		char c4 = 26725;
		System.out.println(c4);
	}
	
	@Test
	public void test05() {
		String helloworld = "helloworld";
		byte[] contents = helloworld.getBytes(StandardCharsets.UTF_8);
		long start = System.currentTimeMillis();
		ByteBuf buf = null;
		for(int i=0,max=300*100*100;i<max;i++) {
			buf = PooledByteBufAllocator.DEFAULT.buffer(1024);
			buf.writeBytes(contents);
			buf.release();
		}
		System.out.println(buf);
		long end = System.currentTimeMillis();
		
		System.out.println(end - start);
	}
	
	@Test
	public void test06() {
		String helloworld = "helloworld";
		byte[] contents = helloworld.getBytes(StandardCharsets.UTF_8);
		long start = System.currentTimeMillis();
		ByteBuf buf = null;
		for(int i=0,max=300*100*100;i<max;i++) {
			buf = Unpooled.buffer(1024);
			buf.writeBytes(contents);
			buf.release();
		}
		System.out.println(buf);
		long end = System.currentTimeMillis();
		
		System.out.println(end - start);
	}
	
}
