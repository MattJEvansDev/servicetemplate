package voteservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@SpringBootApplication
public class VoteServiceBoot {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(VoteServiceBoot.class, args);

    }

}


