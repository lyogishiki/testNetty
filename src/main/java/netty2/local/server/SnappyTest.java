package netty2.local.server;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.xerial.snappy.Snappy;


public class SnappyTest {

	public static byte[] compressHtml(String html) {
		try {
			return Snappy.compress(html, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("编码错误！");
		}
	}

	public static String decompressHtml(byte[] bytes) {
		try {
			return Snappy.uncompressString(bytes, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException();
		}
	}

	public static void main(String[] args) throws IOException {
		String str = "_biz=MjM5NDMwNjMzNA==&mid=2651805828&idx=1&sn=2f413828d1fdc6a64bdbb25c51508dfc&scene=2&srcid=0519iChOETxAx0OeGoHnm7Xk&fro";
		byte[] bytes = compressHtml(str);
		String str2 = decompressHtml(bytes);
		System.out.println(str);
		System.out.println(str2);
		System.out.println(str.getBytes().length);
		System.out.println(bytes.length);
		System.out.println(bytes);
		System.out.println(Snappy.compress(str.getBytes()).length);
	}

}
