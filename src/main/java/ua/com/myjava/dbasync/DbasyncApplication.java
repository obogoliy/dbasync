package ua.com.myjava.dbasync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@ComponentScan(basePackageClasses = DbasyncApplication.class)
@SpringBootApplication
public class DbasyncApplication {

	public static void main(String[] args) {
		SpringApplication.run(DbasyncApplication.class, args);
	}

}
