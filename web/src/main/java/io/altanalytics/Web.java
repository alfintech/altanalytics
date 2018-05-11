package io.altanalytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:web.properties")
public class Web {

	public static void main(String[] args) {
		SpringApplication.run(Web.class);
	}
}
