package tech.trvihnls;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@SpringBootApplication
public class GoLinguageServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoLinguageServerApplication.class, args);
    }
}
