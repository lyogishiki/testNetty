package netty2.time;

import java.io.Serializable;
import java.util.Date;

public class UpdTime implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private final long currentTime;

	public long getCurrentTime() {
		return currentTime;
	}

	public UpdTime(long currentTime) {
		super();
		this.currentTime = currentTime;
	}

	@Override
	public String toString() {
		return "UpdTime [currentTime=" + ThreadLocalFormatter
				.getInstance()
		.getDateFormat().format(new Date(currentTime)) + "]";
	}
	
	
}
