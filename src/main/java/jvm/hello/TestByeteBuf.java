package jvm.hello;

import io.netty.buffer.ByteBufAllocator;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;
import io.netty.util.internal.ReflectionUtil;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;

public class TestByeteBuf {
	
	public static void main(String[] args) {
//		ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(20);
//		
//		System.out.println(buf.toString());
//		System.out.println(buf.getClass());
//		String str = "123456798989485690456740567045"
//				+ "67904567904576094576047646456456456"
//				+ "4567456745674567345763456tergher"
//				+ "2536tergbhsdbvbvvvvvvvaaa1";
//		buf.writeBytes(str.getBytes(StandardCharsets.UTF_8));
//		System.out.println(buf);
//		buf.readInt();
//		System.out.println(buf);
//		buf.retain();
//		System.out.println(buf);
//		System.out.println(buf);
//		buf.readerIndex(0);
		
//		ReferenceCountUtil.release(str)
		ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(10);
		buf.writeBytes("abcdefghi0".getBytes(StandardCharsets.UTF_8));
		
//		buf.release();
		ReferenceCountUtil.release(buf);
		
		System.out.println(buf);
		
	}
}
