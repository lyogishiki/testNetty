package netty2.chapter10;

import io.netty.channel.CombinedChannelDuplexHandler;

public class CombinedByteCharCodec 
extends CombinedChannelDuplexHandler<ByteToCharDecoder, CharToByteEncoder>{

	public CombinedByteCharCodec() {
		super(new ByteToCharDecoder(),new CharToByteEncoder());
	}

}
