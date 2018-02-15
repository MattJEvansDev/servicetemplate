# A Dockerised Spring Boot Service Template
This project acts as a service template to build Java based Microservices from.
The concept is taken from Sam Newman's "Building Microservices".

As well as making it quicker for you to get up and running with your service. It also aims to provide:
* A common approach to logging - **TODO**
* A common approach to authentication - **TODO**
* A common approach to metrics - **TODO**
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


## Deployment instructions
This project allows you to specify a version and name through a gradle task.
This means you currently don't need to change any config files.

It's as simple as running the following command 

 Build the Jar
```
./gradlew clean build -PserviceName=testService -PserviceVersion=0.1.2  docker 
```
Simply change -PserviceName and -PserviceVersion as appropriate
To check your images exists, you can run 

```
docker images
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

