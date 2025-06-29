package com.example.Bida.Bida.Bida;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EntityScan("com.example.Bida.Bida.Bida")
@EnableJpaRepositories("com.example.Bida.Bida.Bida")
@EnableScheduling
public class BidaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BidaApplication.class, args);
	}

}
