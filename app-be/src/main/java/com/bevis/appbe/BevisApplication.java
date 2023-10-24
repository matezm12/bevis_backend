package com.bevis.appbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("com.bevis")
@EnableJpaRepositories("com.bevis")
@EntityScan("com.bevis")
@EnableConfigurationProperties({LiquibaseProperties.class})
public class BevisApplication {

    public static void main(String[] args) {
        SpringApplication.run(BevisApplication.class, args);
    }

}
