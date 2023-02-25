package netty2.udp.java;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class UDPClient {

	public static void main(String[] args) throws Exception {
		// InetAddress address =
		// Inet4Address.get
		// InetAddress address = InetAddress.getByName("127.0.0.1");
		DatagramSocket socket = new DatagramSocket();

		int port = 28080;
		InetSocketAddress address = new InetSocketAddress("192.168.2.233", port);

		for (int i = 0; i < 1; i++) {
			String str = "用户名:1234,密码:2234.-----" + i;
			byte[] data = str.getBytes(StandardCharsets.UTF_8);
			DatagramPacket packet = new DatagramPacket(data, data.length, address);
			// 发送数据
			socket.send(packet);
		}

		byte[] data = "end".getBytes(StandardCharsets.UTF_8);
		DatagramPacket packet = new DatagramPacket(data, data.length, address);
		// 发送数据
		socket.send(packet);

		byte[] data2 = new byte[2048];
		DatagramPacket packet2 = new DatagramPacket(data2, data2.length);
		socket.receive(packet2);

		String reply = new String(data2, 0, packet2.getLength());
		System.out.println("我是客户端,收到:" + reply);

		socket.close();

	}

}
