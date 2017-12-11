package netty2;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.common.reflect.Invokable;
import com.google.common.reflect.Reflection;
import com.google.common.reflect.TypeToken;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

public class TestCase {

	
	public static void main(String[] args) throws InterruptedException {
		ByteBuf buf = Unpooled.buffer(1024);
		System.out.println(buf.getClass());
		
		ByteBuf buf2 = Unpooled.buffer();
		
		System.out.println(buf.capacity());
		System.out.println(buf.maxCapacity());
		
		
		System.out.println(buf2.capacity());
		System.out.println(buf2.maxCapacity());
		
		System.out.println(Integer.MAX_VALUE);
		
//		byte[] bytes = new byte[Integer.MAX_VALUE];
//		byte[] bytes2 = new byte[Integer.MAX_VALUE];
//		byte[] bytes3 = new byte[Integer.MAX_VALUE/3];
//		byte[] bytes4 = new byte[Integer.MAX_VALUE/3];
		
//		Thread.sleep(100000);
		//byte[] bytes2 = new byte[Integer.MAX_VALUE];
		
		System.out.println(buf.hasArray());
		System.out.println(buf.array());
		System.out.println(ByteBuffer.allocateDirect(1024).hasArray());
		
//		System.out.println(ByteBuffer.allocateDirect(1024).array());
		
		System.out.println(ByteBuffer.allocateDirect(1024).getClass());
	
		
		test02();
	}
	
	void test01(ByteBuffer buffer) {
		if(buffer.isDirect()) {
			
			int length = buffer.remaining();
			byte[] array = new byte[length];
			buffer.get(array);
			
		}
	}
	
	static void test02(){
		System.out.println("TestCase.test02()");
		CompositeByteBuf messageBuf = 
				Unpooled.compositeBuffer();
		
		ByteBuf head = Unpooled.directBuffer(1024);
		ByteBuf body = Unpooled.buffer(1024);
		
		messageBuf.addComponents(head,body);
		
		System.out.println(head == messageBuf.component(0));
		System.out.println(head);
		System.out.println(messageBuf.component(0));
	}
	
	@Test
	public void test03() throws NoSuchFieldException, SecurityException {
		Field field = Processor.class.getField("i");
		
		System.out.println(Modifier.isStatic(field.getModifiers()));
		
		
		
	}
	
	@Test
	public void test04() {
		ByteBufAllocator.DEFAULT.buffer();
//		ByteBufHolder holder = ;
	}
}
