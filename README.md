# A Dockerised Spring Boot Service Template
This project acts as a service template to build Java based Microservices from.
The concept is taken from Sam Newman's "Building Microservices".

As well as making it quicker for you to get up and running with your service. It also aims to provide:
* A common approach to logging - **TODO**
* A common approach to authentication - **TODO**
* A common approach to metrics 
* Anything else useful that is not domain specific !

### Prerequisites
* Docker is installed on your local box
* You are running JDK 1.8 or above
* Branches have been setup with GitFlow https://github.com/nvie/gitflow
* This project uses gradle wrapper

## Information on the webservice example
Like I said, this is a template, it uses the example of a voting service (because I wanted to test something in spring data's page library).
It exposes a rest endpoint that lets you get the last vote, or a paginated list of votes.

Please note; I wouldn't advise using PageImpl in the way that used here.
The "totalElements" and "totalPages" methods do not always give the correct results when implemented this way. Please see See https://jira.spring.io/browse/DATACMNS-798

In reality you'll be writing your own web services anyway!

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
This project allows you to specify a version through a gradle task.
This means you currently don't need to change any config files.
The name of the service is derived from the project name. 

It's as simple as running the following command 

 Build the Jar
```
./gradlew clean build -PserviceVersion=0.1.2  docker 
```
Simply change -PserviceVersion as appropriate
To check your images exists, you can run 

```
docker images
```

To run your docker image:
```
docker run --name test-instance -p 8083:8083 -d servicetemplate/0.1.2
```

To test the deployment go to:
Go to http://localhost:8083/vote/last , expected outcome "1"

## Notes
 * As it stands, the gradle wrapper files have been added into the repostiory. 
 Although this is not ideal, there's a problem when running "gradle wrapper" and
 using the docker plugin. I'm not sure whether this is my configuration or the plugin
 so I will continue to investigate it's use before raising an issue.

## License
This project uses the Apache 2.0 License, please see LICENSE.md for a copy

For information on opensource projects that are have been used, please check OPENSOURCE-LICENSES.md


## Acknowledgements
Thanks to Sam Newman for his work on Microservices and  Michael Nygard for his book Release it!
If you read two books this year, read Sam and Michael's!

Thanks to Palantir Technologies for there Docker gradle plugin work - https://github.com/palantir/gradle-docker

