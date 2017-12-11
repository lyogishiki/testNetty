package netty.code;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.msgpack.MessagePack;
import org.msgpack.template.Templates;
import org.msgpack.type.ArrayValue;
import org.msgpack.type.Value;

public class MessagepackTest {

	public static void main(String[] args) throws IOException {
		List<String> src = new ArrayList<>();
		src.add("msgPack");
		src.add("kumofs");
		src.add("viver");
		MessagePack msgPack = new MessagePack();
		byte[] raw = msgPack.write(src);
		System.out.println(raw.length);
		List<String> dst = msgPack.read(raw,Templates.tList(Templates.TString));
		System.out.println(dst);
		
	
		UserInfo info = new UserInfo();
		info.buildUserId(15).buildUserName("zhangsan");
		byte[] raw2 = msgPack.write(info);
		Value info2 = msgPack.read(raw2);
		//info2.
		System.out.println(info2.isArrayValue());
		System.out.println(info2.isBooleanValue());
		System.out.println(info2.isFloatValue());
		System.out.println(info2.isIntegerValue());
		System.out.println(info2.isMapValue());
		System.out.println(info2.isNilValue());
		System.out.println(info2.isRawValue());
		ArrayValue arrayValue = info2.asArrayValue();
//		arrayValue.ge
	}
}
