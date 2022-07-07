package org.infoshare.rekinyfinansjeryweb;

import org.infoshare.rekinyfinansjeryweb.service.CreateUserService;
import org.infoshare.rekinyfinansjeryweb.service.extrernalApi.ExternalDataApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RekinyFinansjeryWebApplication {
		public static void main(String[] args) {
		SpringApplication.run(RekinyFinansjeryWebApplication.class, args);
	}
}

