package netty2.pri;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class NettyMessage<T> implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private byte type;		//消息类型
	
	private Header header;
	private T body;
	
	public NettyMessage() {
		super();
	}
	
	public NettyMessage(byte type, Header header, T body) {
		super();
		this.type = type;
		this.header = header;
		this.body = body;
	}

	public NettyMessage(byte type) {
		super();
		this.type = type;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}
	
	 /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "NettyMessage [type = " + type + " , header = " + header + " , body = " + body + "]";
    }

	public static class Header implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private int length;		//消息长度
		private long sessionID;	//会话ID
	
		private byte priority;	//消息优先级
		private Map<String, Object> attachment = new HashMap<>();	//附件
		
		public int getLength() {
			return length;
		}
		public void setLength(int length) {
			this.length = length;
		}
		public long getSessionID() {
			return sessionID;
		}
		public void setSessionID(long sessionID) {
			this.sessionID = sessionID;
		}
		
		public byte getPriority() {
			return priority;
		}
		public void setPriority(byte priority) {
			this.priority = priority;
		}
		public Map<String, Object> getAttachment() {
			return attachment;
		}
		public void setAttachment(Map<String, Object> attachment) {
			this.attachment = attachment;
		}
		
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "Header [length=" + length + ", sessionID=" + sessionID
					+ ", priority=" + priority + ", attachment=" + attachment + "]";
		}
		
	}
	
	public static void main(String[] args) {
		HashMap<String, String> map = new HashMap<>();
		map.entrySet();
	}
}


