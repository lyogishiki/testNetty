package netty.io;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.nio.ByteBuffer;
public class NTimeServer {

	public static void main(String[] args) {
		int port = 8080;
		
		MultiplexerTimeServer server  = new MultiplexerTimeServer(port);
		new Thread(server).start();
	}
}

class MultiplexerTimeServer implements Runnable{

	private Selector selector;
	private ServerSocketChannel servChannel;
	private volatile boolean stop;
	
	
	
	public MultiplexerTimeServer(int port) {
		super();
		try {
			selector = Selector.open();
			servChannel = ServerSocketChannel.open();
			servChannel.configureBlocking(false);
			servChannel.bind(new InetSocketAddress(port));
			servChannel.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("NIO start in port : " + port);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}

	public void stop(){
		this.stop = true;
	}

	@Override
	public void run() {
		while(!stop){
			try {
				int num = selector.select(5000);
				if(num == 0){
					System.out.println("等待链接");
					continue;
				}
				Set<SelectionKey> keys = selector.selectedKeys();
				Iterator<SelectionKey> it = keys.iterator();
				while(it.hasNext()){
					SelectionKey key = it.next();
					it.remove();
					try{
						handleInput(key);
					}catch(Exception e){
						if(key != null){
							key.cancel();
							if(key.channel()!=null){
								key.channel().close();
							}
						}
					}
				}
//				keys.parallelStream().forEach((key) -> {
//					keys.remove(key);
//					
//				});
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//多路复用器官不候，所有注册在上面的channel和pipe等资源
//		都会被自动区资源并关闭，所以不需要重复释放资源
		if(selector != null){
			try {
				selector.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	

	private void handleInput(SelectionKey key) throws IOException{
		
		if(key.isValid()){
			if(key.isAcceptable()){
				ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
				SocketChannel sc = ssc.accept();
				sc.configureBlocking(false);
				sc.register(selector, SelectionKey.OP_READ);
			}
			
			if(key.isReadable()){
				SocketChannel sc = (SocketChannel) key.channel();
				ByteBuffer readBuffer = ByteBuffer.allocate(1024);
				int readBytes = sc.read(readBuffer);
				
				if(readBytes > 0){
					readBuffer.flip();
					Charset charset = Charset.forName("utf-8");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String line = charset.decode(readBuffer).toString() + sdf.format(new Date());
					System.out.println(line);
					doWrite(sc,line);
				}else if(readBytes < 0){
					key.channel();
					sc.close();
				}
				//0字节 忽略
			}
		}
	}

	//没有考虑写半包的情况
	private void doWrite(SocketChannel sc, String line) throws IOException {
		if(line!=null && line.trim().length()>0){
			byte[] bytes = line.getBytes("utf-8");
			ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
			writeBuffer.put(bytes);
			writeBuffer.flip();
			sc.write(writeBuffer);
			
		}
	}

	
}
