package param;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DefaultFuture {

	private long id;
	private Response response;
	
	private Lock lock = new ReentrantLock();
	
	private Condition condition = lock.newCondition();
	
	public final static Map<Long, Response> map = new ConcurrentHashMap<>();

	
	public Response get() throws InterruptedException {
		lock.lock();
		try {
			while (!hasDone()) {
				condition.await();
			} 
			return response;
		} finally {
			lock.unlock();
		}
	}
	
	
	public boolean hasDone() {
		return response != null;
	}
}
