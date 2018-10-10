package netty2.local;

import netty2.local.client.EchoClient;
import netty2.local.server.EchoServer;

public class Demo {
	
	public static void main(String[] args) throws Exception {
		
		
		new Thread(() -> {
			try {
				new EchoServer().start();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
		
		Thread.sleep(5000);
		
		new Thread(() -> {
			try {
				new EchoClient().start();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

		
		
	}

}
