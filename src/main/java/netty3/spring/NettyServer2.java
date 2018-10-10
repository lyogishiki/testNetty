package netty3.spring;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;


@Component
public class NettyServer2 implements ApplicationListener<ApplicationReadyEvent>,Ordered{

	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		System.out.println("NettyServer2.onApplicationEvent()");
	}


	
}
