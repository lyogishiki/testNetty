package jvm.nio.mulity;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

public class Client {

	public static void main(String[] args) {
		CountDownLatch latch = new CountDownLatch(1000);
		for (int i = 0; i < 1000; i++) {
			new Thread(() -> {
				Socket socket = new Socket();
				try {
					socket.connect(new InetSocketAddress("127.0.0.1", 8888), 10000);
					for(int j=0;j<1000;j++) {
						socket.getOutputStream().write("1234567890awqsderfu".getBytes(StandardCharsets.UTF_8));
					}
					latch.countDown();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						socket.close();
					} catch (IOException e) {
					}
				}
			}).start();
		}
		
		try {
			Thread.sleep(10000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(latch.getCount());
	}

}
