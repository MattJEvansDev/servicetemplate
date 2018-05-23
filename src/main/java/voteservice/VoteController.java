/**
 * Copyright 2018 Matt J Evans
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions andlimitations under the License.
 *
 */
package voteservice;


import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public static final String DEFAULT_ERROR = "Oh No.... There's no instances of this service, I should probably handle this... Nahh";
    private static final Logger LOG = LoggerFactory.getLogger(VoteController.class);
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private EurekaClient eurekaClient;

    @Autowired
    private WordServiceClient wordServiceClient;

    private LoadBalancerClient loadBalancerClient;

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

        return new ResponseEntity<String>(wordServiceClient.getRandomWord(), HttpStatus.OK);
    }

    // An Example of how to get service discovery information from Eureka server without Feign Client
    private String getWordService() {
        Application application = eurekaClient.getApplication("wordService");

        if (application == null) {
            return DEFAULT_ERROR;
        }
        List<InstanceInfo> instances = application.getInstances();

        if (!instances.isEmpty()) {
            InstanceInfo instanceInfo = instances.get(0);
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


        return DEFAULT_ERROR;
    }
}
