package netty2.base.protostuff;

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
import netty2.pri.NettyMessage;
import netty2.pri.NettyMessage.Header;

/**
 * RuntimeSchema.getSchema 自身是带有缓存机制的，不需要担心每次获取schema都会重新生成，
 * 无需额外的缓存。
 * @author ghost
 *
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
		NettyMessage<List<Student>> nettyMessage = new NettyMessage<>();
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
		
		System.out.println(nettyMessage2);
	}
}
