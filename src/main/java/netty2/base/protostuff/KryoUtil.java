package netty2.base.protostuff;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import netty2.pri.NettyMessage;
import netty2.pri.NettyMessage.Header;

public class KryoUtil {

	public static void main(String[] args) {
		Kryo kryo = new Kryo();
		kryo.register(NettyMessage.class);
		kryo.register(Map.class);
		kryo.register(List.class);
		
		NettyMessage<Student> nettyMessage = new NettyMessage<>();
		Header header = new Header();
		header.setPriority((byte)1);
		header.setSessionID(ThreadLocalRandom.current().nextLong());
		header.setLength(155);
		Map<String, Object> attachment = new HashMap<>();
		attachment.put("AA", "AA");
		attachment.put("BB", "BB");
		header.setAttachment(attachment);
		nettyMessage.setHeader(header);
		
		Student student = new Student("12345", "zhangsan", 15, "北大街");
		nettyMessage.setBody(student);
		
		ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
		Output output = new Output(baos2);
		kryo.writeObject(output, nettyMessage);
		output.flush();
		System.out.println(baos2.size());
		
		Input in = new Input(baos2.toByteArray());
		
		NettyMessage<Student> nettyMessage2 = kryo.readObject(in, NettyMessage.class);
		System.out.println(nettyMessage2);
	}
	
}
