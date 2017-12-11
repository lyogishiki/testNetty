package netty.factorial;

import java.math.BigInteger;

public class TestCase {
	
	public static void main(String[] args) {
		BigInteger b = new BigInteger("1234");
		System.out.println(b);
		byte[] bytes = b.toByteArray();
		System.out.println(bytes);
	}
}
