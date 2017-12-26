package netty2.pri;

import java.util.HashSet;
import java.util.Set;

public class WhiteContext {

	private static Set<String> whitekList = new HashSet<>(16);

	static {
		whitekList.add("127.0.0.1");
		whitekList.add("192.168.0.120");
		whitekList.add("192.168.1.2");
	}
	
	public static boolean isWhite(String ip) {
		return whitekList.contains(ip);
	}

}
