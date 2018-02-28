package voteservice;


import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Matt on 26/01/2018.
 */
@RestController
@RequestMapping("/vote")
public class VoteController {

    @Bean
    public RestTemplate template() {
        return new RestTemplate();
    }
    @Autowired
    private RestTemplate restTemplate;

    public static final List<Integer> PAGE_VALUES = Arrays.asList(1, 3, 5, 1, 7, 8, 10, 12, 15, 30, 1, 5, 5, 1);
    private static final Logger LOG = LoggerFactory.getLogger(VoteController.class);

    @RequestMapping(method = RequestMethod.GET, value = "/last")
    public ResponseEntity<Integer> fetchLast() {
        LOG.info("Getting last vote");

        Integer voteValue = PAGE_VALUES.get(0);
        HttpStatus resultantStatus = voteValue == null ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return new ResponseEntity<Integer>(voteValue, resultantStatus);
    }


    @RequestMapping(method = RequestMethod.GET,value ="/downstream")
    public ResponseEntity<String> callDownstream(@RequestHeader HttpHeaders headers){

        setHeaderWithCrednetials(headers);

        HttpEntity<String> request = new HttpEntity<String>(headers);

        // restTemplate must be a bean so that the logback interceptors can inject traceIds !!
        ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:8084/word/random", HttpMethod.GET, request, String.class);
        LOG.info("My response is "+responseEntity.getBody());
        return new ResponseEntity<String>(responseEntity.getBody(),HttpStatus.OK);
    }

    private void setHeaderWithCrednetials(HttpHeaders headers) {
        String plainCreds = "admin:password";
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);

        headers.add("Authorization", "Basic " + base64Creds);

        LOG.info("Headers are :" + headers.toString());

    }

}
