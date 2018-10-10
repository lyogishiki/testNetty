package netty3.first;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.google.common.primitives.Chars;

import io.netty.buffer.PooledByteBufAllocator;

public class TestCase {

	@Test
	public void test01() {
		System.out.println(5 & 3);
	}
	
	
	@Test
	public void test02() throws IOException, InterruptedException {
		Runtime runtime = Runtime.getRuntime();
		
		Process process = runtime.exec("ping 127.0.0.1");
		
		IOUtils.readLines(process.getInputStream(),Charset.forName("GBK")).forEach(System.out::println);
	
		
		
		System.out.println(process.waitFor());
		
		System.exit(-2);
	}
	
	private static int test001() {
		System.exit(-1);
		return 1;
	}
	
	public static void main(String[] args) throws ClassNotFoundException {
		//不初始化 的情况下 不会调用static方法
//		Class.forName("netty3.first.Man",false,TestCase.class.getClassLoader());
//		"$0$"
//		Class.forName("netty3.first.Man");
		
		PooledByteBufAllocator pooled = PooledByteBufAllocator.DEFAULT;
		pooled.buffer();
//		pooled.directBuffer()
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			int i = 1/0;
		}));
		
		try {
			test001();
			
			System.out.println(1);
		} catch (Exception e) {
			// MYTASK Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
