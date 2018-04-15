package voteservice;


import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import voteservice.clients.WordServiceClient;


import java.util.Arrays;
import java.util.List;

/**
 * Created by Matt on 26/01/2018.
 */
@RestController
@RequestMapping("/vote")
public class VoteController {

    public static final List<Integer> PAGE_VALUES = Arrays.asList(1, 3, 5, 1, 7, 8, 10, 12, 15, 30, 1, 5, 5, 1);
    private static final Logger LOG = LoggerFactory.getLogger(VoteController.class);
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private EurekaClient eurekaClient;

    @Autowired
    private WordServiceClient wordServiceClient;

    @Bean
    public RestTemplate template() {
        return new RestTemplate();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/last")
    public ResponseEntity<Integer> fetchLast() {
        LOG.info("Getting last vote");

        Integer voteValue = PAGE_VALUES.get(0);
        HttpStatus resultantStatus = voteValue == null ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return new ResponseEntity<Integer>(voteValue, resultantStatus);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/downstream")
    public ResponseEntity<String> callDownstream(@RequestHeader HttpHeaders headers) {

        String wordServiceURL = getWordService();

        return new ResponseEntity<String>( wordServiceClient.getRandomWord(), HttpStatus.OK);
    }

    // An Example of how to get service discovery information from Eureka server without Feign Client
    private String getWordService() {
        Application application = eurekaClient.getApplication("wordService");
        InstanceInfo instanceInfo = application.getInstances().get(0);
        String hostname = instanceInfo.getHostName();
        int port = instanceInfo.getPort();

        StringBuilder builder = new StringBuilder();
        builder
                .append("http://")
                .append(hostname)
                .append(":")
                .append(port)
                .append("/word/random");

        LOG.info("wordService url is :" + builder.toString());
        return builder.toString();
    }
}
