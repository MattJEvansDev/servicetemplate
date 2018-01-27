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

## Things to change
Like I said, this is a template, it uses the example of a voting service (because I wanted to test something in spring data's page library).
It exposes a rest endpoint that lets you get the last vote, or a paginated list of votes.

Please note; I wouldn't advise using PageImpl in the way that used here.
The "totalElements" and "totalPages" methods do not always give the correct results when implemented this way. Please see See https://jira.spring.io/browse/DATACMNS-798

### build.gradle
Change the baseName value from "voteservice" to "nameofmyservice"
Change the version value from "0.1.0" to "X.Y.Z"

### Dockerfile
Change the line
```
ADD build/libs/voteservice-0.1.0.jar voteservice-app.jar
```
to
```
ADD build/libs/nameofmyservice-X.Y.Z.jar nameofmyservice-app.jar
```
Change the line
```
RUN bash -c 'touch /voteservice-app.jar'
```
to
```
RUN bash -c 'touch /nameofmyservice-app.jar'
```
Change the line
```
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/voteservice-app.jar"]
```
to
```
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/nameofmyservice-app.jar"]
```

## Deployment instructions
This assumes you're in the root directory (the one with the Dockerfile).

 Build the Jar
```
./gradlew build
```
 Remove any existing containers
```
docker rm test-instance
```

 build the voteservice image
```
docker build -t voteservice .
```

Start a Docker container
```
docker run --name test-instance -p 8083:8083  voteservice
```

To test the deployment go to:
Go to http://localhost:8083/vote/last , expected outcome "1"

## License
This project uses the MIT License, please see License.md for a copy


## Acknowledgements
Thanks to Sam Newman for his work on Microservices and  Michael Nygard for his book Release it!
If you read two books this year, read Sam and Michael's!

