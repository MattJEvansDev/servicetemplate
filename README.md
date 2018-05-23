# A Dockerised Spring Boot Service Template

This project serves two purposes:
* To act as a service template to build Java based Microservices from.
* To demonstrate a collection of microservices working together (i.e. calling downstream services 
from this serviceTemplate)

#### Service Template
The concept is taken from Sam Newman's "Building Microservices".

As well as making it quicker for you to get up and running with your service. It also aims to provide:
* A common approach to inter-service communication
* A common approach to logging 
* A default fallback strategy for external calls - **TODO**
* A common approach to authentication - **TODO**
* A common approach to metrics 
* An example of how the project should be deployed (i.e. within Kubernetes)
* Anything else useful that is not domain specific !

#### Microservices working together
This template has been designed considering both development and deployment to an environment. 
Consequently it can be deployed locally using either the gradlew bootRun task or onto a Kuberenetes environment. 

However, if you want to see Microservices working together, you're likely to want to go for the latter option.

For running locally, install [minikube](https://kubernetes.io/docs/getting-started-guides/minikube/) 
and refer to [Running within Kuberentes](#running-within-kubernetes).
 
### Prerequisites
* Docker is installed on your local box
* Minikube installed (if you want to deploy downstream services)
* You are running JDK 1.8 or above
* This project uses gradle wrapper

## Service Discovery
This series of projects provides two approaches towards service discovery:
* Using [Netflix's Eureka](https://github.com/Netflix/eureka) as a service discovery tool
* Using Kuberentes as a service discovery mechanism

In reality, you'll more than likely choose one mechanism, however for the purposes of illustration, both have been included.

Requests to the serviceTemplate are load balanced and routed through a Kuberenetes load balancer; taking advantage 
of service discovery to identify instances of the serviceTemplate.

The downstream service (wordservice) registers with the Eureka cluster and it's information is retrieved (from Eureka)
by the sericeTemplate

You can set up your own Eureka server (and the associated Kubernetes deployment), 
or to make things a little easier, clone [an example server](https://github.com/MattJEvansDev/eurekaServerExample).

## Inter-Service Communication
A Eureka server is used as the service discovery mechanism in the project. For those new to service discovery, 
think of it as a DNS. You register with a discovery server if you're expose a service, 
and you ask for information on services if you're a client (yes, you can be a service and a client).

The @EnableDiscoveryClient annotation is used to register this service with the Eureka server.
There are two ways to get service information from the server:
 1) Using the getWordService() method VoteController to manually fetch information from the server
 2) Using the Netflix Feign Client ; by implementing the @FeignClient interface, services are evaluated at run time. 

The implementation example of the feign client, provides a fallback mechanism in the event that the downstream service 
is unavailable.

## Logging

This template uses Logback to configure logging. 

The app exposes two example endpoints, one returning a result with no further calls (vote/last)
and one that calls an external webservice (/word/random).

The purpose of the /word/random service call is to illustrate log ID correlation.

The quickest way to view correlation ids is to start all three services within Kubernetes (see 
[Running within Kuberentes](#running-within-kubernetes)) and then do the following:
 
1) Launch the serviceTemplate in a browser
```
minikube svc servicetemplate-lb
```
2) Append "/vote/downstream" to the url from step 1.
3) View the log files using
```
kubectl logs servicetemplate-<HASH_GENEREATED_BY_KUBERENETES> | less
kubectl logs wordservice-<HASH_GENEREATED_BY_KUBERENETES> | less
```

If you don't want to run the full Kuberenetes demo (i.e. only want to run jars), do the following:
 1) Clone this project
 2) Clone [the example Microservice](https://github.com/MattJEvansDev/wordservice)
 3) Clone [the example Eureka server](https://github.com/MattJEvansDev/eurekaServerExample).
 4) Start the Eureka Server
 5) Start the example Microservice using ./gradlew bootRun
 6) Start this project using ./gradlew bootRun
 7) go to localhost:8083/vote/downstream
 8) checkout the logs and notice the id is passed between the different services
 
Note: The reason we have to run the services outside of Docker  is so that it can
talk to the docker container through localhost.


## Metrics
To allow consistency across Microservices (regardless of implementation language), metrics are exposed over
a restful url taking the following format:
```
localhost:8083/manage/{id} 
```
A service template implementation for a second language should follow the same url, regardless to what metrics it 
chooses to expose (i.e. JVM Heap size is not applicable in C#).

Spring Actuator is used to generate pull standard Java metrics from the app. This handy library exposes metrics at 
a snapshot in time over restful endpoints. The metrics are exposed over 

For example 
```
localhost:8083/manage/info
localhost:8083/manage/metrics
localhost:8083/manage/env 
```
For more information on Spring Actuator endpoints, see 
https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-endpoints.html

**Note** Certain Actuator endpoints require authentication (e.g. /metrics) so credentials have been defined in 
application.properties. In reality, you probably wouldn't store these credentials here! 

An example of a custom end point has been defined to illustrate how to capture service specific metrics 
```
localhost:8083/manage/exampleEndpoint 
```

Tools such as Prometheus can be used to get metrics from a container level and make use of the information exposed at 
these endpoints. 

## Deployment instructions

### Building the project
This project allows you to specify a version through a gradle task.
This means you currently don't need to change any config files.
The name of the service is derived from the project name. 

It's as simple as running the following command 

 Build the Jar
```
./gradlew clean build -PserviceVersion=0.1.2  dockerTag 
```
Simply change -PserviceVersion as appropriate
To check your images exists, you can run 

```
docker images
```

### Running within Docker
To run your docker image:
```
docker run --name test-instance -p 8083:8083 -d mjevans/servicetemplate
```

To test the deployment go to:
Go to http://localhost:8083/vote/last , expected outcome "1"

### Running within Kubernetes
To get the most out of this serviceTemplate, it makes sense to run within Kubernetes.
The Kuberentes configuration of this project is shown below 

![Overview of Architecture](kubernetes/serviceTemplate-diagram.jpg?raw=true "Overview")

Kuberenetes performs load balancing between the serviceTemplate instances.
Each of the serviceTemplate instances will register with the Eureka cluster (again via a Kuberenetes 
loadbalancer, however Eureka will ensure the state of the cluster is eventually consistent).
The word service registers with Eureka cluster, allowing the serviceTemplate instances to discovery it
without the need of further load balancers. Steps 2-5 in the Overview diagram illustrate the flow 
for communicating with a downstream service

To deploy all the services, do the following.

1) Tell minikube to point to your local docker repository
```
eval $(minikube docker-env)
```
2) Create the serviceTemplate load balancer service and serviceTemplate deployment
```
kubectl apply -f servicetemplate-lb.json --record
kubectl apply -f servicetemplate-deployment.json --record
```
3) Clone the [the example Eureka server](https://github.com/MattJEvansDev/eurekaServerExample)
 project and create the Eureka Loadbalancer and deployment
```
kubectl apply -f eureka-lb.json --record
kubectl apply -f eureka-deployment.json --record
```
4) Clone the [downstream word service](https://github.com/MattJEvansDev/wordservice) project and deploy it
```
kubectl apply -f wordservice-deployment.json --record
```

For more detailed instructions, refer to the README for each  project.


## Notes
 * As it stands, the gradle wrapper files have been added into the repostiory. 
 Although this is not ideal, there's a problem when running "gradle wrapper" and
 using the docker plugin. I'm not sure whether this is my configuration or the plugin
 so I will continue to investigate it's use before raising an issue.

## License
This project uses the Apache 2.0 License, please see LICENSE.md for a copy

A report of open source licenses can be generated using the gradle task
```
./gradlew downloadLicenses
```

This generates a html document within build/reports/license
To view third party testing libraries, set 
```
dependencyConfiguration = 'compileTest'
```

## Acknowledgements
Thanks to Sam Newman for his work on Microservices and  Michael Nygard for his book Release it!
If you read two books this year, read Sam and Michael's!

Thanks to Palantir Technologies for there Docker gradle plugin work - https://github.com/palantir/gradle-docker

Thanks to all the Open Source projects and contributors that've helped !
