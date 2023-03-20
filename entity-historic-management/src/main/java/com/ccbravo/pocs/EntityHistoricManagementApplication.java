package com.ccbravo.pocs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EntityHistoricManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(EntityHistoricManagementApplication.class, args);
	}

}
