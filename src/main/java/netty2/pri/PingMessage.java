package netty2.pri;

public class PingMessage{
	/**
	 * 
	 */
	private byte value;

	public PingMessage(byte value) {
		super();
		this.value = value;
	}

	public byte getValue() {
		return value;
	}

	public void setValue(byte value) {
		this.value = value;
	}
	
	
	
}
