package pt.ua.tqsenv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SpringBootReactApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootReactApplication.class, args);
    }
}
