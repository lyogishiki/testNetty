package netty.code;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

import org.msgpack.annotation.Message;

@Message
public class UserInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8076924906671174427L;

	private String userName;
	private int userId;
	
	public UserInfo buildUserId(int userId){
		this.userId = userId;
		return this;
	}
	
	public UserInfo buildUserName(String userName){
		this.userName = userName;
		return this;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + userId;
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserInfo other = (UserInfo) obj;
		if (userId != other.userId)
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	public byte[] codeC(){
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte[] value = this.userName.getBytes();
		buffer.putInt(value.length);
		buffer.put(value);
		buffer.putInt(this.userId);
		buffer.flip();
		value = null;
		byte[] result = new byte[buffer.remaining()];
		buffer.get(result);
		return result;
	}
	

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		UserInfo userInfo = new UserInfo();
		userInfo.buildUserId(100).buildUserName("Hello Netty!AHAHAHA");
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(userInfo);
		oos.flush();
		oos.close();
		
		byte[] b = bos.toByteArray();
		System.out.println(b.length);
		System.out.println(userInfo.codeC().length);
		
		ByteArrayInputStream bis = new ByteArrayInputStream(b);
		ObjectInputStream ois = new ObjectInputStream(bis);
		UserInfo userInfo2 = (UserInfo) ois.readObject();
		ois.close();
		System.out.println(userInfo == userInfo2);
		System.out.println(userInfo.equals(userInfo2));
	}
	
}
