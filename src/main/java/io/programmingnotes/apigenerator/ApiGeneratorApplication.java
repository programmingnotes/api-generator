package io.programmingnotes.apigenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories("io.programmingnotes.apigenerator.repository")
public class ApiGeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGeneratorApplication.class, args);
	}

}
