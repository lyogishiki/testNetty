package netty2.embedded;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;

public class AbsIntegerEncoderTest {
	
	@Test
	public void testEncoder() {
		ByteBuf buf = Unpooled.buffer();
		
		for(int i=1;i<10;i++) {
			buf.writeInt(-1 * i);
		}
		
		EmbeddedChannel channel = new EmbeddedChannel(
				new AbsIntegerEncoder());
		
		
		assertTrue(channel.writeOutbound(buf));
		
		assertTrue(channel.finish());
		
		for(int i=1;i<10;i++) {
			int j = channel.readOutbound();
			System.out.println(i + ":" + j);
			assertEquals(i, j);
		}
		
		assertNull(channel.readOutbound());
	}

}
