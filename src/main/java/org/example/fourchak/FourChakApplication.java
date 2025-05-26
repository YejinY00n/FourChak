package org.example.fourchak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaAuditing
@EnableCaching
@EnableTransactionManagement
@SpringBootApplication
public class FourChakApplication {

    public static void main(String[] args) {
        SpringApplication.run(FourChakApplication.class, args);
    }

}
