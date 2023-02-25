package netty2.udp.java;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class TestSocketServer {

	public static void main(String[] args) throws IOException {
		ServerSocket ss = new ServerSocket(28080);
		
		while(true) {
			Socket socket = ss.accept();
			InputStream in = socket.getInputStream();
			while(true) {
				byte[] data = new byte[10240];
				int len = in.read(data);
				System.out.println("接收:" + new String(data,0,len));
			}
		}
	}
	
}
