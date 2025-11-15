package com.edu.silva.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class UsersApplication {

	static void main(String[] args) {
		SpringApplication.run(UsersApplication.class, args);
	}

}
