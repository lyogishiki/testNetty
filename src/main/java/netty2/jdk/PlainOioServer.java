package netty2.jdk;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class PlainOioServer {

	public void server(int port) throws IOException {
		ServerSocket serverSocket = new ServerSocket(port);
		
		for(;;) {
			Socket socket = serverSocket.accept();
			new Thread(() -> {
				try {
					BufferedOutputStream bos = 
							new BufferedOutputStream(socket.getOutputStream());
					bos.write("Hi PengYou".getBytes());
					bos.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}finally {
					try {
						socket.close();
					} catch (IOException e) {}
				}
				
			}).start();
			
		}
	}
	
}
