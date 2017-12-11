
package netty2.jdk;

import java.nio.channels.SelectionKey;

public class TestCase {
	
	public static void main(String[] args) {
		System.out.println(SelectionKey.OP_ACCEPT);
		System.out.println(SelectionKey.OP_CONNECT);
		System.out.println(SelectionKey.OP_READ);
		System.out.println(SelectionKey.OP_WRITE);
		
		System.out.println(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
		
		System.out.println(SelectionKey.OP_ACCEPT | SelectionKey.OP_CONNECT);
		System.out.println(SelectionKey.OP_ACCEPT & SelectionKey.OP_CONNECT);
	}

}
