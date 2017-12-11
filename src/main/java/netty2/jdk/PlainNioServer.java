package netty2.jdk;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import io.netty.channel.ServerChannel;

public class PlainNioServer {

	public void start(int port) throws IOException {
		ServerSocketChannel serverSocketChannel = 
				ServerSocketChannel.open();
		
		serverSocketChannel.bind(new InetSocketAddress(port));
//		LocalEventLoopGroup
		
		serverSocketChannel.configureBlocking(false);
		
		Selector selector = Selector.open();
		//注册selector接收连接
		serverSocketChannel.register(selector, 
				SelectionKey.OP_ACCEPT);
		
		final ByteBuffer msg = ByteBuffer.wrap("Hi,PongYou!".getBytes());
		
		for(;;) {
			
			int num = selector.select(3000);
			
			if(0 == num) {
				continue;
			}
			
			Set<SelectionKey> keys = selector.selectedKeys();
			Iterator<SelectionKey> it = keys.iterator();
			
			while(it.hasNext()) {
				
				SelectionKey key = it.next();
				it.remove();
				
				try {
					
					if(key.isAcceptable()) {
						ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
						SocketChannel client = serverChannel.accept();
						
						client.register(selector, 
								SelectionKey.OP_WRITE|SelectionKey.OP_READ,
								msg.duplicate());
					}
					
					if(key.isWritable()) {
						SocketChannel clietn = (SocketChannel) key.channel();
						ByteBuffer msgCopy = (ByteBuffer) key.attachment();
						
						while(msgCopy.hasRemaining()) {
							if(clietn.write(msgCopy) == 0) {
								break;
							}
						}
						clietn.close();
					}
					
				}catch (Exception e) {
					key.cancel();
					try {
						key.channel().close();
					} catch (Exception e1) {}
				}
			
				
			}
		}
	}
	
}
