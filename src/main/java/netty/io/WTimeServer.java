package netty.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class WTimeServer {

	public static void main(String[] args) {
		int port = 8080;
		
		ServerSocket serverSocket = null;
		int coreCount = Runtime.getRuntime().availableProcessors();
		try{
			serverSocket = new ServerSocket(port);
			ExecutorService service = new ThreadPoolExecutor(coreCount, 
					3*coreCount, 120, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(512));
			while(true){
				Socket socket = serverSocket.accept();
				service.execute(new TimeServerHandler(socket));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(serverSocket!=null){
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

