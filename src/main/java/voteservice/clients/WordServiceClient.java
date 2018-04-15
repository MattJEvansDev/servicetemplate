package voteservice.clients;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "wordService", configuration = WordServiceConfiguration.class)
public interface WordServiceClient {

    @RequestMapping("/word/random")
    String getRandomWord();
}
