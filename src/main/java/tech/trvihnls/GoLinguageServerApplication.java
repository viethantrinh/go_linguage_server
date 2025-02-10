package tech.trvihnls;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GoLinguageServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoLinguageServerApplication.class, args);
    }
}
