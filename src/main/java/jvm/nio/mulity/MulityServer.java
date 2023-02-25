package jvm.nio.mulity;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

// 同一个线程 阻塞时 怎么让他执行其他的.
//技巧
//自定义线程池处理selector
public class MulityServer {

	public static void main(String[] args) throws IOException {
		new MulityServer().start();
	}

	private void start() throws IOException {
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.configureBlocking(false);
		Selector boss = Selector.open();
		ssc.register(boss, SelectionKey.OP_ACCEPT);
		ssc.bind(new InetSocketAddress(8888));

		WorkerLoop workerLoop = new WorkerLoop(10);
		workerLoop.start();
		
		while (true) {
			int select = boss.select();
			if (select == 0) {
				continue;
			}
			Iterator<SelectionKey> it = boss.selectedKeys().iterator();

			while (it.hasNext()) {
				SelectionKey key = it.next();
				it.remove();

				if (key.isAcceptable()) {
					ServerSocketChannel channel = (ServerSocketChannel) key.channel();
					SocketChannel sc = channel.accept();
					sc.configureBlocking(false);

					try {
						workerLoop.addWork(sc);
					} catch (ClosedChannelException e) {
						key.cancel();
					}
				}
			}
		}
	}

	class WorkerLoop {
		private Worker[] workers;
		private int workerCount;

		private AtomicInteger ai = new AtomicInteger(0);

		public WorkerLoop(int workerCount) {
			super();
			this.workers = new Worker[workerCount];
			this.workerCount = workerCount;
			for (int i = 0; i < workerCount; i++) {
				this.workers[i] = new Worker("worker-" + i);
			}
		}

		public void start() throws IOException {
			for (Worker worker : workers) {
				worker.start();
			}
		}

		public void addWork(SocketChannel sc) throws ClosedChannelException {
			workers[ai.getAndIncrement() % this.workerCount].addWork(sc);
			if (ai.get() >= this.workerCount) {
				ai.set(0);
			}
		}

	}

	class Worker implements Runnable {
		private Thread thread;
		private Selector worker;
		private String name;
		private volatile boolean start = false;
		private ConcurrentLinkedQueue<Runnable> tasks = new ConcurrentLinkedQueue<>();

		public Worker(String name) {
			this.name = name;
		}

		public void start() throws IOException {
			if (!start) {
				synchronized (this) {
					if (!start) {
						start = true;
						this.worker = Selector.open();
						this.thread = new Thread(this, this.name);
						this.thread.start();
					}
				}
			}
		}

		public void addWork(SocketChannel sc) throws ClosedChannelException {
			long start = System.currentTimeMillis();
			tasks.add(() -> {
				// selector的select方法的执行会阻塞register方法的执行..
				// 所以执行register的时候,不能执行select,
				try {
					sc.register(worker, SelectionKey.OP_READ);
				
				} catch (ClosedChannelException e) {
					e.printStackTrace();
				}
				long end = System.currentTimeMillis();
//				System.out.println("添加客户端链接。" + (end - start) + "ms");
			});
			// 唤醒selector,让他执行我们的register方法.
			worker.wakeup();
		}

		@Override
		public void run() {
			while (true) {
				try {
					int select = worker.select(2000);

					while (tasks.peek() != null) {
						Runnable task = tasks.poll();
						task.run();
					}
					if (select == 0) {
						continue;
					}

					Iterator<SelectionKey> it = worker.selectedKeys().iterator();
					while (it.hasNext()) {
						SelectionKey key = it.next();
						it.remove();
						deal(key);
					}

				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}

		private void deal(SelectionKey key) {
			try {
				if (key.isReadable()) {
					SocketChannel sc = (SocketChannel) key.channel();
					ByteBuffer buf = ByteBuffer.allocate(1024);
					int len = sc.read(buf);
					if (len == -1) {
//						System.out.println(Thread.currentThread().getName() + "客户端关闭连接");
						key.cancel();
						return;
					}

					buf.flip();
					byte[] data = new byte[len];
					buf.get(data);
					System.out.println(System.currentTimeMillis() + ":" + 
							Thread.currentThread().getName() + ":收到数据:" + new String(data, StandardCharsets.UTF_8));
				}

				if (key.isWritable()) {

				}
			} catch (Exception e) {
				e.printStackTrace();
				key.cancel();
				try {
					key.channel().close();
				} catch (Exception e1) {
				}
			}
		}
	}

}
