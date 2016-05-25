package com.insidecoding.opium.agent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@ComponentScan
@EnableAutoConfiguration
@EnableScheduling
@EnableSwagger2
public class Application {

  public static void main(String... args) throws Exception {
    SpringApplication.run(Application.class, args);
  }
}
