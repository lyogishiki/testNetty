package netty2.pri;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;

import io.netty.channel.Channel;

public class ServerContext {

	private ServerContext() {
		
	};
	
	private static final int DEFAULT_SIZE = 64;
	
	private static final ConcurrentHashMap<Channel, String> loginUser = new ConcurrentHashMap<Channel, String>(DEFAULT_SIZE);
	private static final ConcurrentHashMap<String, Channel> loginUserInverse = new ConcurrentHashMap<>(DEFAULT_SIZE);
	
	public static boolean isLogin(@Nonnull Channel channel) {
		return loginUser.containsKey(channel);
	}
	
	public static boolean isLogin(@Nonnull String address) {
		return loginUserInverse.containsKey(address);
	}
	
	public static void removeLoginUser(@Nonnull Channel channel) {
		Objects.requireNonNull(channel,"Channel不能为空！");
		String value = loginUser.remove(channel);
		if(value != null) {
			loginUserInverse.remove(value);
		}
	}
	
	public static void userLogin(@Nonnull Channel channel,@Nonnull String address) {
		Objects.requireNonNull(channel,"channel 不能为空！");
		Objects.requireNonNull(address, "地址不能为空！");
		
		loginUser.put(channel, address);
		loginUserInverse.put(address, channel);
	}
	
	public static boolean isWhite(String ip) {
		return WhiteContext.isWhite(ip);
	}
	
	public static void main(String[] args) {
//		不是线程安全的 不能在这里使用
//		BiMap<String, Channel> loginChannel;
	}
	
}
