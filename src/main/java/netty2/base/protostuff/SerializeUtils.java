package netty2.base.protostuff;

import static org.mockito.Matchers.startsWith;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import netty2.base.protostuff.Father.Children;
import netty2.pri.MessageType;
import netty2.pri.NettyMessage;
import netty2.pri.NettyMessage2;
import netty2.pri.PingMessage;
import netty2.pri.client.HeartBeatReqHandler;
import netty2.pri.server.HeartBeatRespHandler;
import netty2.pri.NettyMessage.Header;

/**
 * RuntimeSchema.getSchema 自身是带有缓存机制的，不需要担心每次获取schema都会重新生成，
 * 无需额外的缓存。
 * @author ghost
 */
public class SerializeUtils {

	@SuppressWarnings("unchecked")
	public static <T> byte[] serializer(T t) {
		return ProtobufIOUtil.toByteArray(t, 
				RuntimeSchema.getSchema((Class<T>) t.getClass()), 
				LinkedBuffer.allocate());

	}

	public static <T> T deSerialize(byte[] data, Class<T> clazz) {
		Schema<T> runtimeSchema = RuntimeSchema.getSchema(clazz);
		T t = runtimeSchema.newMessage();
		ProtobufIOUtil.mergeFrom(data, t, runtimeSchema);
		return t;
	}
	
	public static void main(String[] args) throws IllegalAccessException {
		/*NettyMessage<List<Student>> nettyMessage = new NettyMessage<>();
		Header header = new Header();
		header.setPriority((byte)1);
		header.setSessionID(ThreadLocalRandom.current().nextLong());
		header.setType((byte)1);
		header.setLength(155);
		Map<String, Object> attachment = new HashMap<>();
		attachment.put("AA", "AA");
		attachment.put("BB", "BB");
		header.setAttachment(attachment);
		nettyMessage.setHeader(header);
		
		List<Student> list = new ArrayList<>();
		for(int i=0;i<100;i++) {
			Student student = new Student("12345", "zhangsan", i, "北大街");
			list.add(student );
		}
		nettyMessage.setBody(list);
		
		byte[] bytes = serializer(nettyMessage);
		System.out.println(bytes.length);
		NettyMessage<Student> nettyMessage2 = deSerialize(bytes, NettyMessage.class);
		
		System.out.println(nettyMessage2);*/
		
		/*NettyMessage<Void> PING_MESSAGE = new NettyMessage<>();
		Header header = new Header();
		header.setType(MessageType.HEARTBEAT_REQ);
		PING_MESSAGE.setHeader(header);*/
		
		/*NettyMessage2<Void> ping = new NettyMessage2<>((byte)1);
		
		System.out.println(serializer(HeartBeatReqHandler.PING_MESSAGE).length);
		System.out.println(serializer(HeartBeatRespHandler.PONG_MESSAGE).length);
		byte[] bytes = serializer(HeartBeatReqHandler.PING_MESSAGE);
		
		Object s = deSerialize(bytes, Object.class);
		System.out.println(s);*/
		
		/*Children c = new Children();
		c.setId("1");
		c.setName("2");
		
		byte[] bytes = serializer(c);
		
		Father f = deSerialize(bytes, Father.class);
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("1", "2");
		map.put("3", "4");
		
		
		byte[] bytes2 = serializer(map);
		
		Object obj = deSerialize(bytes2, Object.class);
		
		System.out.println(obj);*/
		
		Map<String, Object> map = new HashMap<>();
		map.put("1", "2");
		map.put("3", "4");
		
		NettyMessage<Map<String, Object>> nettyMessage = new NettyMessage<>();
		nettyMessage.setBody(map);
		
		byte[] bytes = serializer(nettyMessage);
		NettyMessage<Map<String, Object>> nettyMessage2 = deSerialize(bytes, NettyMessage.class);
		
		System.out.println(nettyMessage2);
		System.out.println(nettyMessage2.getBody().getClass());
		
		Children c = new Children();
		c.setId("1");
		c.setName("2");
		NettyMessage<Children> nettyMessage3 = new NettyMessage<>();
		nettyMessage3.setBody(c);
		
		byte[] data = serializer(nettyMessage3);
		NettyMessage<Father> nettyMessage4 = deSerialize(data, NettyMessage.class);
		
		System.out.println(nettyMessage4);
		System.out.println(nettyMessage4.getBody() instanceof Children);
		
	}
}
