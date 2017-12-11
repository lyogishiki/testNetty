package netty.quote;

import io.netty.buffer.Unpooled;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

import netty.Consts;

public class TestCase {

	public static void main(String[] args) {
		DatagramPacket dg = new DatagramPacket(
				Unpooled.copiedBuffer("QOTM?", CharsetUtil.UTF_8), 
				new InetSocketAddress("255.255.255.255", Consts.PORT));
		
		String msg = dg.content().toString(CharsetUtil.UTF_8);
		System.out.println(msg);
	}
}
