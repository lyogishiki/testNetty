package jvm.nio;

import java.awt.RenderingHints.Key;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import io.netty.channel.Channel.Unsafe;
import io.netty.channel.unix.Buffer;

public class Client {

	public static void main(String[] args) throws IOException {
//		SocketChannel sc = SocketChannel.open()
		SocketChannel sc = SocketChannel.open();
//		sc.configureBlocking(false);
//		sc.read(null)
//		sc.connect(null)
//		sc.isConnected();
		Selector selector = Selector.open();
		boolean flag = sc.connect(new InetSocketAddress(8888));
		System.out.println(flag);
		sc.register(selector, SelectionKey.OP_CONNECT);

		_loop: while (true) {
			int select = selector.select(2000);
			if (select == 0) {
				continue;
			}

			Iterator<SelectionKey> it = selector.selectedKeys().iterator();
			while (it.hasNext()) {
				SelectionKey key = it.next();
				// it.remove必须调用,不然这个selectionKey会一直存在在selectedKeys集合中.
				it.remove();

				if (key.isConnectable()) {
					SocketChannel channel = (SocketChannel) key.channel();

					// 非阻塞模式要调用finishConnect....
					// System.out.println(channel.finishConnect());
					if (channel.finishConnect()) {
						System.out.println(channel.isConnected());
						channel.register(selector, SelectionKey.OP_WRITE);
					}
				}

				if (key.isWritable()) {
					ByteBuffer buf = (ByteBuffer) key.attachment();
					if (buf == null) {
						System.out.println("第一次，=,没有附件");
						StringBuilder sb = new StringBuilder();
						for (int i = 0; i < 10; i++) {
							sb.append("hello111222333444!");
						}
						sb.append("$$$");
						byte[] data = sb.toString().getBytes(StandardCharsets.UTF_8);
						int len = data.length;

						buf = ByteBuffer.allocate(4 + len);
						// buf.putInt(10000);
						// buf.put("end!".getBytes(StandardCharsets.UTF_8));
						buf.putInt(len);
						buf.put(data);
						buf.flip();
						key.attach(buf);
					}
					SocketChannel channel = (SocketChannel) key.channel();

					int r = channel.write(buf);
					System.out.println("写入:" + r + "字节数据");
					if (!buf.hasRemaining()) {
						// 写入结束
						System.out.println("写入动作完成!");
						key.attach(null);
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}
						 channel.close();
						 break _loop;
					}

					// ByteBuffer data = ByteBuffer.
					// data.putInt(select)

					// 返回值 代表一次写入内容的字节数。有可能一次写不完buffer.
					// 可以循环调用。
					// 循环调用卡死线程,不推荐.
					// while (buf.hasRemaining()) {
					// int r = channel.write(buf);
					// System.out.println("写入:" + r + "字节,剩余:" + buf.remaining() + "字节");
					// }

					// int r = channel.write(buf);

					// buf.flip();
					// channel.write(buf);

					// key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
				}

			}

		}
		// System.out.println("结束");
	}

}
