package netty2.base.protostuff;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.util.ReferenceCountUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import netty2.pri.NettyMessage;
import netty2.pri.NettyMessage.Header;

public class TestCase {

	private int max = 400 * 100 * 100;

	@Test
	public void testProtosuf() {
		NettyMessage<Student> nettyMessage = new NettyMessage<>();
		Header header = new Header();
		header.setPriority((byte) 1);
		header.setSessionID(ThreadLocalRandom.current().nextLong());
		header.setLength(155);
		Map<String, Object> attachment = new HashMap<>();
		attachment.put("AA", "AA");
		attachment.put("BB", "BB");
		header.setAttachment(attachment);
		nettyMessage.setHeader(header);
		Student student = new Student("12345", "zhangsan", 15, "北大街");
		nettyMessage.setBody(student);

		byte[] data = null;
		NettyMessage<Student> nettyMessage2 = null;

		long start = System.currentTimeMillis();

		for (int i = 0; i < max; i++) {
			data = SerializeUtils.serializer(nettyMessage);
		}

		long end1 = System.currentTimeMillis();

		for (int i = 0; i < max; i++) {
			nettyMessage2 = SerializeUtils.deSerialize(data, NettyMessage.class);
		}

		long end2 = System.currentTimeMillis();

		System.out.println(end1 - start);
		System.out.println(end2 - end1);
		System.out.println(nettyMessage2);
	}

	@Test
	public void testKryo() {

		NettyMessage<Student> nettyMessage = new NettyMessage<>();
		Header header = new Header();
		header.setPriority((byte) 1);
		header.setSessionID(ThreadLocalRandom.current().nextLong());
		header.setLength(155);
		Map<String, Object> attachment = new HashMap<>();
		attachment.put("AA", "AA");
		attachment.put("BB", "BB");
		header.setAttachment(attachment);
		nettyMessage.setHeader(header);
		Student student = new Student("12345", "zhangsan", 15, "北大街");
		nettyMessage.setBody(student);

		byte[] data = null;
		NettyMessage<Student> nettyMessage2 = null;

		long start = System.currentTimeMillis();
		Kryo kryo = new Kryo();
		kryo.register(NettyMessage.class);
		kryo.register(Map.class);
		kryo.register(List.class);
		kryo.register(Student.class);

		for (int i = 0; i < max; i++) {
			Output output = new Output(2048);
			kryo.writeObject(output, nettyMessage);
			output.flush();
			data = output.toBytes();
		}

		long end1 = System.currentTimeMillis();

		for (int i = 0; i < max; i++) {
			Input input = new Input(data);
			nettyMessage2 = kryo.readObject(input, NettyMessage.class);
		}

		long end2 = System.currentTimeMillis();

		System.out.println(end1 - start);
		System.out.println(end2 - end1);
		System.out.println(nettyMessage2);

	}

	@Test
	public void test01() {
		Schema<NettyMessage> schema = null;
		for (int i = 0; i < 100 * 100 * 100 * 10; i++) {
			schema = RuntimeSchema.getSchema(NettyMessage.class);
		}

		System.out.println(schema);
	}

	int max2 = 100 * 100 * 100 * 100;

	@Test
	public void test02() {
		String msg = "hello world!hello world!dg234523卧推4热污染根523翻过去为34有退卡45903㐇【个撇开发过火3042件哦呜胸话i5ugjebjrtfn2e4opri把你  ；2人";

		long start = System.currentTimeMillis();
		ByteBuf tmp = null;
		for(int i=0;i<max2;i++) {
			
			tmp = ByteBufUtil.encodeString(PooledByteBufAllocator.DEFAULT, CharBuffer.wrap(msg), StandardCharsets.UTF_8);
			ReferenceCountUtil.release(tmp);
		}
		System.out.println(tmp);
		long end = System.currentTimeMillis();
		
		System.out.println(end - start);
	}
	
	@Test
	public void test03() {
		String msg = "hello world!hello world!dg234523卧推4热污染根523翻过去为34有退卡45903㐇【个撇开发过火3042件哦呜胸话i5ugjebjrtfn2e4opri把你  ；2人";

		long start = System.currentTimeMillis();
		ByteBuf tmp = null;
		for(int i=0;i<max2;i++) {
			ByteBuf buf = PooledByteBufAllocator.DEFAULT.buffer(512);
			buf.writeBytes(msg.getBytes(StandardCharsets.UTF_8));
			tmp = buf;
			ReferenceCountUtil.release(tmp);
		}
		System.out.println(tmp);
		long end = System.currentTimeMillis();
		
		System.out.println(end - start);
	}
	
	
}
