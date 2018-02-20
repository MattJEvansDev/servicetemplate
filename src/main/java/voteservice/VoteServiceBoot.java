package voteservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {"manage","voteservice"})
public class VoteServiceBoot {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(VoteServiceBoot.class, args);

    }

}


