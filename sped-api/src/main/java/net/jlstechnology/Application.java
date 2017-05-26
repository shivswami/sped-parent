package net.jlstechnology;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
	
	private Application() {}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}

}
