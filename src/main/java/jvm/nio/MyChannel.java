package jvm.nio;

import java.nio.ByteBuffer;

public class MyChannel {

	public ByteBuffer buffer;
	public int type = 0;
	public int length = 0;
	
	@Override
	public String toString() {
		return "MyChannel [buffer=" + buffer + ", type=" + type + ", length=" + length + "]";
	}
	
	
}
