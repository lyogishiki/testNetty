package test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.concurrent.ScheduledExecutorTask;

public class TestCommit {

	public static void main(String[] args) throws InterruptedException {
		 Semaphore semaphore = new Semaphore(0);
		 semaphore.acquireUninterruptibly();
		// System.out.println(semaphore.availablePermits());
		// semaphore.release();
		// System.out.println(semaphore.availablePermits());
		// semaphore.acquire();
		//
		//// System.out.println(1);
		// System.out.println(semaphore.availablePermits());
	
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleWithFixedDelay(() -> {
			System.out.println(1 + "," + System.currentTimeMillis());
		}, 0, 5, TimeUnit.SECONDS);

		executor.scheduleWithFixedDelay(() -> {
			System.out.println(2 + "," + System.currentTimeMillis());
		}, 0, 6, TimeUnit.SECONDS);
	}

}
