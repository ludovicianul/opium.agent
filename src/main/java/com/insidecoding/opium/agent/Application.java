package com.insidecoding.opium.agent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableScheduling
@EnableSwagger2
@SpringBootApplication
public class Application {

    public static void main(String... args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
}
