package voteservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {"voteservice", "voteservice.manage"})
@EnableFeignClients
@EnableDiscoveryClient
public class VoteServiceBoot {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(VoteServiceBoot.class, args);

    }
}


