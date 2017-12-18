package netty2.embedded;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.intThat;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.util.back.ReferenceCountUtil;

public class FrameChunkDecoderTest {

	@Test
	public void testFrameDecoderd() {
		ByteBuf buf = Unpooled.buffer();

		for (int i = 0; i < 9; i++) {
			buf.writeByte(i);
		}
		
		ByteBuf input = buf.duplicate();
		
		EmbeddedChannel channel = new EmbeddedChannel(
				new FrameChunkDecoder(3));
		
		assertTrue(channel.writeInbound(input.readBytes(2)));
		
		try {
			channel.writeInbound(input.readBytes(4));
			fail();
		}catch (TooLongFrameException e) {
			///System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		assertTrue(channel.writeInbound(input.readBytes(3)));
		assertTrue(channel.finish());
		
		ByteBuf read = channel.readInbound();
		assertEquals(buf.readSlice(2),read);
		read.release();
		
		read = channel.readInbound();
//		System.out.println(buf.skipBytes(4).readBytes(3));
//		System.out.println(read);
		ByteBuf buf2 = buf.slice(6, 9);
		System.out.println(read.readByte() + ":" 
				+ read.readByte() + ":" 
				+ read.readByte());
		
		System.out.println(buf2.readByte() + ":" 
				+ buf2.readByte() + ":" 
				+ buf2.readByte());
		assertEquals(buf2, read);
//		assertEquals(, read);
		
		
		
		read.release();
//		ReferenceCountUtil.release(read);
		buf.release();
	}

}
