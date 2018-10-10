package netty3.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootApplication
public class ServerMain {

	public static void main(String[] args) {
		
		
		SpringApplication.run(ServerMain.class, args);
	}
	
}
