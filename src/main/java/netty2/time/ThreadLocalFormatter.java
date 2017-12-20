package netty2.time;

import java.text.SimpleDateFormat;

public class ThreadLocalFormatter {

	public static final ThreadLocalFormatter FORMATTER = new ThreadLocalFormatter();
	
	public static ThreadLocalFormatter getInstance() {
		return FORMATTER;
	}
	
	private ThreadLocalFormatter() {
		
	}
	
	private ThreadLocal<SimpleDateFormat> tl = ThreadLocal.withInitial(() -> 
		new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	
	public SimpleDateFormat getDateFormat() {
		return tl.get();
	}
	
}
