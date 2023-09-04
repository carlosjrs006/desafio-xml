package com.desafios.projetos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EntityScan(basePackages = {"com.desafios.projetos.model","io.codejournal.maven.xsd2java"})
@ComponentScan(basePackages = {"com.desafios.projetos.*"})
@EnableJpaRepositories(basePackages = {"com.desafios.projetos.repository"})
@EnableTransactionManagement
@EnableWebMvc
@RestController
@EnableAutoConfiguration
public class ProjetosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjetosApplication.class, args);
	}

}
