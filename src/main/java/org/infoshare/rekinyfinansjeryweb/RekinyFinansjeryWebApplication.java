package org.infoshare.rekinyfinansjeryweb;

import org.infoshare.rekinyfinansjeryweb.service.ExternalDataApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RekinyFinansjeryWebApplication {

	@Autowired
	ExternalDataApiService externalDataApiService;

	public static void main(String[] args) {
		SpringApplication.run(RekinyFinansjeryWebApplication.class, args);
	}

	@Bean
	public CommandLineRunner CommandLineRunnerBean() {
		return (args) -> {
			externalDataApiService.getData();
		};
	}
}

