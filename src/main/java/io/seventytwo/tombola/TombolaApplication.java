package io.seventytwo.tombola;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TombolaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TombolaApplication.class, args);
    }

}
