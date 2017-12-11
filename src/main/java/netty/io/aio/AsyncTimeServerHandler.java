package netty.io.aio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;


public class AsyncTimeServerHandler implements Runnable {

	private int port;
	
	CountDownLatch latch;
	AsynchronousServerSocketChannel asynchronousServerSocketChannel;
	
	
	public AsyncTimeServerHandler(int port) {
		super();
		this.port = port;
		try{
			asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
			asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
			System.out.println("asynchronousServerSocketChannel on port : " + port);
		}catch(IOException e){
			e.printStackTrace();
		}
	}




	@Override
	public void run() {
		latch = new CountDownLatch(1);
		doAccept();
		
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void doAccept() {
		asynchronousServerSocketChannel.accept(this,new AcceptCompletionHandler());
	}
	
	class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel,AsyncTimeServerHandler>{


		@Override
		public void completed(AsynchronousSocketChannel result, AsyncTimeServerHandler attachment) {
			attachment.asynchronousServerSocketChannel.accept(attachment, this);
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			
			result.read(buffer,buffer,new ReadCompletionHandler(result));
		}

		@Override
		public void failed(Throwable exc, AsyncTimeServerHandler attachment) {
			attachment.latch.countDown();
		}

	}
	
	class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer>{

		private AsynchronousSocketChannel channel = null;
		
		
		
		public ReadCompletionHandler(AsynchronousSocketChannel channel) {
			super();
			if(this.channel == null){
				this.channel = channel;
			}
		}

		@Override
		public void completed(Integer result, ByteBuffer attachment) {
			attachment.flip();
			byte[] body = new byte[attachment.remaining()];
			attachment.get(body);
			
			try {
				String req = new String(body,"UTF-8");
				System.out.println("new Time Server receive order : " + req);
				SimpleDateFormat sdf = new SimpleDateFormat();
				doWrite(sdf.format(new Date()));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		private void doWrite(String currentTime) {
			if(currentTime != null && currentTime.length() > 0){
				byte[] bytes = currentTime.getBytes();
				ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
				writeBuffer.put(bytes);
				writeBuffer.flip();
				
				channel.write(writeBuffer,writeBuffer,new CompletionHandler<Integer, ByteBuffer>() {

					@Override
					public void completed(Integer result, ByteBuffer attachment) {
						if(attachment.hasRemaining()){
							channel.write(attachment,attachment,this);
						}
					}

					@Override
					public void failed(Throwable exc, ByteBuffer attachment) {
						try{
							channel.close();
						}catch(Exception e){
							
						}
					}
				});
			}
		}

		@Override
		public void failed(Throwable exc, ByteBuffer attachment) {
//			if(exc.getCause() instanceof IOException){
//				
//			}
			try {
				this.channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static void main(String[] args) {
		AsyncTimeServerHandler atsh = new AsyncTimeServerHandler(8080);
		atsh.run();
	}

}
