package netty2.embedded;

import static org.junit.Assert.*;

import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;

public class FixedLengthFrameDecoderTest {

	@Test
	public void testFramesDecoder() {
		ByteBuf buf = Unpooled.buffer();
		
		for(int i=0;i<9;i++) {
			buf.writeByte(i);
		}
		ByteBuf input = buf.duplicate();
		
		
		EmbeddedChannel channel = new EmbeddedChannel(
				new FixedLengthFrameDecoder(3));
		
		assertTrue(channel.writeInbound(input.retain()));
		
		//channel.finish() 标记Channel为已完成
		assertTrue(channel.finish());
		
		ByteBuf tmp1 = buf.readSlice(3);
		ByteBuf tmp2 = buf.readSlice(3);
		ByteBuf tmp3 = buf.readSlice(3);
		
		
//		读取所产生的消息，并且验证
		ByteBuf read = channel.readInbound();
		assertEquals(tmp1, read);
		read.release();
		
		read = channel.readInbound();
		assertEquals(tmp2, read);
		read.release();
		
		read = channel.readInbound();
		assertEquals(tmp3, read);
		read.release();
		
		System.out.println(tmp3);
		System.out.println(read);
		System.out.println(tmp2);
		System.out.println(tmp1);
		
		assertNull(channel.readInbound());
		
		buf.release();
		
		
	}
	
	@Test
	public void testFramesDecoded2() {
		ByteBuf buf = Unpooled.buffer();
		for(int i=0;i<9;i++) {
			buf.writeByte(i);
		}
		
		ByteBuf input = buf.duplicate();
		
		EmbeddedChannel channel = new EmbeddedChannel(
				new FixedLengthFrameDecoder(3));
		
//		先写2，在写7，所以最后一共是写了9
		assertFalse(channel.writeInbound(
				input.readBytes(2)));
		assertTrue(channel.writeInbound(
				input.readBytes(7)));
		
		assertTrue(channel.finish());
		
		ByteBuf read = channel.readInbound();
		assertEquals(buf.readSlice(3), read);
		read.release();
		
		read = channel.readInbound();
		assertEquals(buf.readSlice(3), read);
		read.release();
		
		read = channel.readInbound();
		assertEquals(buf.readSlice(3), read);
		read.release();
		
		buf.release();
	}
	
}
