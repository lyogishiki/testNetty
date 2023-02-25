package netty2.udp.java;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

import javax.xml.crypto.Data;

public class UDPServer {

	public static void main(String[] args) throws IOException {
		DatagramSocket socket = new DatagramSocket(28080);
		byte[] data = new byte[10240];

		DatagramPacket packet = new DatagramPacket(data, data.length);
		System.out.println(packet.getAddress());
		System.out.println("服务端启动");

		// 接收数据前会一直阻塞
		String info = null;
		do {
			socket.receive(packet);
			InetAddress a = packet.getAddress();
			SocketAddress sa = packet.getSocketAddress();
			info = new String(data, 0, packet.getLength());
			System.out.println("接收到客户端消息:" + info + "," + a + "," + sa);
		} while (!"end".equals(info));

		InetAddress address = packet.getAddress();
		int port = packet.getPort();
		byte[] data2 = "收到,欢迎".getBytes(StandardCharsets.UTF_8);

		DatagramPacket packet2 = new DatagramPacket(data2, data2.length, address, port);
		socket.send(packet2);

		socket.close();
	}
}
