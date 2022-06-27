package org.infoshare.rekinyfinansjeryweb;

import org.infoshare.rekinyfinansjeryweb.hibernate.Crypto;
import org.infoshare.rekinyfinansjeryweb.hibernate.EntityManagerProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

@SpringBootApplication
public class RekinyFinansjeryWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(RekinyFinansjeryWebApplication.class, args);
		final EntityManager entityManager = EntityManagerProvider.get();
		EntityManagerProvider.startTransaction(entityManager);
		createCrypto(entityManager);
		EntityManagerProvider.commitTransaction(entityManager);
	}

	public static Crypto createCrypto(EntityManager entityManager) {
		final Crypto crypto = new Crypto();
		crypto.setName("Sharkcoin");
		crypto.setCode("SRK");
		crypto.setBidPrice((float) 23.87);
		crypto.setAskingPrice((float) 28.34);
		entityManager.persist(crypto);
		return crypto;
	}
}