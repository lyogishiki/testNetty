package netty.io.aio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class ATimeClient {

	public static void main(String[] args) {
		new  Thread(new AsyncTimeClientHandler("127.0.0.1", 8080)).start();
		for(int i=0;i<500;i++){
		}
	}
	
}
class AsyncTimeClientHandler implements CompletionHandler<Void, AsyncTimeClientHandler>,Runnable{

	private AsynchronousSocketChannel client;
	private String host;
	private int port;
	private CountDownLatch latch;

	public AsyncTimeClientHandler(String host, int port) {
		super();
		this.host = host;
		this.port = port;
		
		try {
			client = AsynchronousSocketChannel.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	@Override
	public void run() {
		latch = new CountDownLatch(1);
		client.connect(new InetSocketAddress(host,port),this,this);
		
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void completed(Void result, AsyncTimeClientHandler attachment) {
		byte[] req = "Query time".getBytes();
		ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
		writeBuffer.put(req);
		writeBuffer.flip();
		
		client.write(writeBuffer,writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {

			@Override
			public void completed(Integer result, ByteBuffer buffer) {
				if(buffer.hasRemaining()){
					client.write(buffer, buffer, this);
				}else{
					ByteBuffer readBuffer = ByteBuffer.allocate(1024);
					client.read(readBuffer,readBuffer,new CompletionHandler<Integer, ByteBuffer>() {

						@Override
						public void completed(Integer result, ByteBuffer buffer) {
							buffer.flip();
							byte[] bytes = new byte[buffer.remaining()];
							buffer.get(bytes);
							try {
								String body = new String(bytes,"UTF-8");
								System.out.println("this time is " + body);
								latch.countDown();
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
							
						}
						
						@Override
						public void failed(Throwable exc, ByteBuffer attachment) {
							try {
								client.close();
								latch.countDown();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						
					});
					
				}
			}

			@Override
			public void failed(Throwable exc, ByteBuffer buffer) {
				try {
					client.close();
					latch.countDown();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void failed(Throwable exc, AsyncTimeClientHandler attachment) {
		try {
			client.close();
			latch.countDown();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
}
