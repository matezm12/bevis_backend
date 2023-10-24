package com.bevis.inapppurchase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.bevis")
@SpringBootApplication
public class InAppPurchaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(InAppPurchaseApplication.class, args);
    }

}
