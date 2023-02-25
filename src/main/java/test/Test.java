package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.attribute.AclEntryPermission;

import org.apache.commons.math3.analysis.integration.MidPointIntegrator;

import com.alibaba.fastjson.JSONObject;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class Test {
	//
	// static void test() {
	// try {
	// int i = 1 / 0;
	// } catch (Exception e) {
	// System.out.println("发生异常");
	// // try {
	// // Thread.sleep(1000);
	// // } catch (InterruptedException e1) {
	// // e1.printStackTrace();
	// // }
	// test();
	// } finally {
	// System.out.println("finally执行");
	// }
	// }

	public static void main(String[] args) throws FileNotFoundException, IOException {
		// test();
		// // System.out.println(JSONObject.toJSONString(String.class));
		// Class<?> clazz = Class.forName("java.util.Comparator");
		// System.out.println(clazz);
		// clazz.newInstance();
//		File file = new File("D:\\tmp\\1\\1.txt");
//		System.out.println(Files.size(file.toPath()));
//		try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
//		}
//		Files.delete(file.toPath());
		
//		ByteBuf buf = Unpooled.buffer(12);
//		buf.release();
//		buf.writeByte(1);
		
		System.out.println(Integer.MAX_VALUE / 1440);
	}

}
