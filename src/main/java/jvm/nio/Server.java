package jvm.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class Server {

	public static void main(String[] args) throws IOException {
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.configureBlocking(false);
		ssc.bind(new InetSocketAddress(8007));

		Selector selector = Selector.open();
		ssc.register(selector, SelectionKey.OP_ACCEPT);

		while (true) {
			int select = selector.select(2000);
			if (select == 0) {
				continue;
			}

			// System.out.println(selector.selectedKeys().size());
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();
			while (it.hasNext()) {
				SelectionKey key = it.next();
				// it.remove 必须被调用.
				it.remove();

				try {
					if (key.isAcceptable()) {
						ServerSocketChannel channel = (ServerSocketChannel) key.channel();
						SocketChannel sc = channel.accept();
						sc.configureBlocking(false);
						MyChannel myChannel = new MyChannel();
						myChannel.type = 0;
						myChannel.buffer = ByteBuffer.allocate(4);
						sc.register(selector, SelectionKey.OP_READ, myChannel);
					}

					if (key.isReadable()) {
						SocketChannel sc = (SocketChannel) key.channel();
						MyChannel myChannel = (MyChannel) key.attachment();
						System.out.println(myChannel);
						ByteBuffer buffer = myChannel.buffer;
						int n = sc.read(buffer);
						if (n == -1) {
							// 把注册的key,取消掉.
							System.out.println("客户端断开连接.");
							key.cancel();
							key.channel().close();
							continue;
						}else if(n == 0) {
							System.out.println("读到0是什么情况！！！");
						}

						
						if (myChannel.type == 0) {
							buffer.flip();
							int length = buffer.getInt();
							ByteBuffer dataBuffer = ByteBuffer.allocate(length);
							myChannel.type = 1;
							myChannel.buffer = dataBuffer;
							myChannel.length = length;
						} else if (myChannel.type == 1) {
							
							if (buffer.limit() == buffer.position()) {
								buffer.flip();
								byte[] data = new byte[myChannel.length];
								buffer.get(data);
								String dataStr = new String(data, StandardCharsets.UTF_8);
								System.out.println("接收到数据:" + dataStr.getBytes(StandardCharsets.UTF_8).length);

								myChannel.type = 0;
								myChannel.length = 0;
								myChannel.buffer = ByteBuffer.allocate(4);
							}
						}

						// System.out.println(key.attachment());
						// key.attach("new Object:" + sc.hashCode());
						// ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
						// int len = sc.read(lengthBuffer);
						// System.out.println("len:" + len);
						// if (len == -1) {
						// // 客戶端正常断开连接.
						// // 把注册的key,取消掉.
						// System.out.println("客户端断开连接.");
						// key.cancel();
						// continue;
						// }
						// lengthBuffer.flip();
						// int length = lengthBuffer.getInt();
						//
						// ByteBuffer buffer = ByteBuffer.allocate(length);
						// len = 0;
						// do {
						// len = sc.read(buffer);
						// System.out.println(".....读数据ing.....:" + len);
						// } while (len != length);
						//
						// buffer.flip();
						// byte[] data = new byte[len];
						// buffer.get(data);
						// System.out.println("收到消息:" + new String(data, StandardCharsets.UTF_8));
					}
				} catch (Exception e) {
					System.out.println("发生异常！！！要关闭客户端的连接");
					e.printStackTrace();
					key.cancel();
					key.channel().close();
				}
			}
		}
	}

}
