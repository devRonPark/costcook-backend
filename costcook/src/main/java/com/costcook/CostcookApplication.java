package com.costcook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CostcookApplication {

	public static void main(String[] args) {
		SpringApplication.run(CostcookApplication.class, args);
	}

}
