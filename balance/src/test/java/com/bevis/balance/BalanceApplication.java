package com.bevis.balance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.bevis")
@SpringBootApplication
public class BalanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BalanceApplication.class, args);
    }

}
