package com.example.discordia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableJpaAuditing
@RestController
@SpringBootApplication
public class DiscordiaApplication {

	@GetMapping("/")
	public String home() {
		return "Hello World!";
	}

	public static void main(String[] args) {
		SpringApplication.run(DiscordiaApplication.class, args);
	}

}
