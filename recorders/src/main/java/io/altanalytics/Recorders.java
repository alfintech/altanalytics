package io.altanalytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:recorders.properties")
public class Recorders {

	public static void main(String[] args) {
		SpringApplication.run(Recorders.class);
	}
}
