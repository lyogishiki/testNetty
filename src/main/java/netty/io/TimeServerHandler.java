package netty.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeServerHandler implements Runnable{

	private Socket socket;
	
	public TimeServerHandler(Socket socket) {
		super();
		this.socket = socket;
	}

	@Override
	public void run() {
		
		try(BufferedReader bReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter pw = new PrintWriter(socket.getOutputStream(),true)){
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String line = null;
			while((line = bReader.readLine()) != null && !"bye".equals(line)){
				StringBuilder body = new StringBuilder();
				body.append(line);
				body.append(sdf.format(new Date()));
				pw.println(body.toString());
			}
			
		
			pw.flush();
			
		}catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}